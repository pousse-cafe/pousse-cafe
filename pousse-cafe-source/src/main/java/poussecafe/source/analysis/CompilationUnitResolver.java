package poussecafe.source.analysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import poussecafe.discovery.AbstractMessage;
import poussecafe.discovery.Aggregate;
import poussecafe.discovery.DataAccessImplementation;
import poussecafe.discovery.DataImplementation;
import poussecafe.discovery.MessageImplementation;
import poussecafe.discovery.MessageListener;
import poussecafe.discovery.ProducesEvent;
import poussecafe.domain.AggregateFactory;
import poussecafe.domain.AggregateRepository;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.DomainEvent;
import poussecafe.domain.Entity;
import poussecafe.domain.EntityAttributes;
import poussecafe.domain.EntityDataAccess;
import poussecafe.domain.Factory;
import poussecafe.domain.Repository;
import poussecafe.messaging.Message;
import poussecafe.runtime.Command;

import static java.util.Objects.requireNonNull;

@SuppressWarnings("deprecation")
public class CompilationUnitResolver implements Resolver {

    private void init() { // NOSONAR
        loadCompilationUnitClass();
        registerInnerClasses();
    }

    private void loadCompilationUnitClass() {
        var packageName = compilationUnit.getPackage().getName().getFullyQualifiedName();
        var typeName = (AbstractTypeDeclaration) compilationUnit.types().get(0);
        var className = packageName + "." + typeName.getName().getFullyQualifiedName();
        compilationUnitClass = classResolver.loadClass(new Name(className))
                .orElseThrow(() -> newResolutionException(className));
    }

    private CompilationUnit compilationUnit;

    public CompilationUnit compilationUnit() {
        return compilationUnit;
    }

    private ResolvedClass compilationUnitClass;

    private ClassResolver classResolver;

    @Override
    public ClassResolver classResolver() {
        return classResolver;
    }

    private ResolutionException newResolutionException(String className) {
        return new ResolutionException("Unable to load class " + className);
    }

    private void registerInnerClasses() {
        registerInnerClasses(compilationUnitClass);
    }

    private void registerInnerClasses(ResolvedClass containerClass) {
        for(ResolvedClass innerClass : containerClass.innerClasses()) {
            registerClass(innerClass);
            registerInnerClasses(innerClass);
        }
    }

    public void tryRegister(ImportDeclaration importDeclaration) {
        if(!importDeclaration.isStatic()) {
            if(importDeclaration.isOnDemand()) {
                tryRegisterOnDemandImport(importDeclaration);
            } else {
                tryRegisterSingleTypeImport(importDeclaration);
            }
        }
    }

    private void tryRegisterOnDemandImport(ImportDeclaration importDeclaration) {
        String packageName = importDeclaration.getName().getFullyQualifiedName();
        importedPackages.add(packageName);
    }

    private List<String> importedPackages = new ArrayList<>();

    private void tryRegisterSingleTypeImport(ImportDeclaration importDeclaration) {
        Name fullyQualifiedName = new Name(importDeclaration.getName().getFullyQualifiedName());
        registerClass(classResolver.loadClass(fullyQualifiedName)
                .orElseThrow(() -> newResolutionException(fullyQualifiedName.toString())));
    }

    private void registerClass(ResolvedClass importedClass) {
        var className = importedClass.name();
        importedClasses.put(className.toString(), importedClass);
        resolvedTypeNames.put(className.getIdentifier().toString(), resolvedTypeName(importedClass));
    }

    private Map<String, ResolvedClass> importedClasses = new HashMap<>();

    private ResolvedTypeName resolvedTypeName(ResolvedClass resolvedClass) {
        return new ResolvedTypeName.Builder()
            .withResolver(this)
            .withName(resolvedClass.name().getIdentifier())
            .withResolvedClass(resolvedClass)
            .build();
    }

    @Override
    public ResolvedTypeName resolve(Name name) {
        return resolveClass(name);
    }

    private ResolvedTypeName resolveClass(Name name) {
        if(name.isQualifiedName()) {
            var resolvedClass = classResolver.loadClass(name);
            if(resolvedClass.isPresent()) {
                return resolvedTypeName(resolvedClass.get());
            } else {
                return resolvePartiallyQualifiedName(name);
            }
        } else {
            String simpleName = name.toString();
            return resolvedTypeNames.computeIfAbsent(simpleName, key -> resolveSimpleName(name)
                    .orElseThrow(() -> newResolutionException(name.toString())));
        }
    }

    private ResolvedTypeName resolvePartiallyQualifiedName(Name name) {
        if(!name.isQualifiedName()) {
            throw new IllegalArgumentException();
        }
        var innerClassPath = new LinkedList<String>();
        var segments = name.segments();
        for(int i = segments.length - 1; i >= 0; --i) {
            var simpleName = segments[i];
            var resolvedTypeName = resolveSimpleName(new Name(simpleName));
            if(resolvedTypeName.isPresent()) {
                var innerClass = classResolver.loadInnerClass(new Name(resolvedTypeName.get().qualifiedName()),
                        innerClassPath).orElseThrow(() -> newResolutionException(name.toString()));
                return new ResolvedTypeName.Builder()
                        .withName(name)
                        .withResolvedClass(innerClass)
                        .withResolver(this)
                        .build();
            } else {
                innerClassPath.addFirst(simpleName);
            }
        }
        throw new ResolutionException("Unable to resolve " + name);
    }

    private Map<String, ResolvedTypeName> resolvedTypeNames = new HashMap<>();

    private Optional<ResolvedTypeName> resolveSimpleName(Name simpleName) {
        if(simpleName.isQualifiedName()) {
            throw new IllegalArgumentException();
        }

        var resolvedName = resolvedTypeNames.get(simpleName.toString());
        if(resolvedName != null) {
            return Optional.of(resolvedName);
        }

        for(String importedPackage : importedPackages) {
            Optional<ResolvedClass> loadedClass = tryResolution(importedPackage, simpleName);
            if(loadedClass.isPresent()) {
                return Optional.of(resolvedTypeName(loadedClass.get()));
            }
        }
        return tryResolution(compilationUnitPackageName(), simpleName)
                .or(() -> tryResolution("java.lang", simpleName))
                .or(() -> tryResolution("", simpleName))
                .map(this::resolvedTypeName);
    }

    private String compilationUnitPackageName() {
        return compilationUnit.getPackage().getName().getFullyQualifiedName();
    }

    private Optional<ResolvedClass> tryResolution(String packageName, Name simpleName) {
        if(simpleName.isQualifiedName()) {
            throw new IllegalArgumentException();
        }

        Name candidateQualifiedName;
        if(packageName.isEmpty()) {
            candidateQualifiedName = simpleName;
        } else {
            candidateQualifiedName = new Name(packageName + "." + simpleName);
        }

        return classResolver.loadClass(candidateQualifiedName);
    }

    public ResolvedMethod resolve(MethodDeclaration method) {
        return new ResolvedMethod.Builder()
                .withResolver(this)
                .withDeclaration(method)
                .build();
    }

    public static final String AGGREGATE_ROOT_CLASS = AggregateRoot.class.getCanonicalName();

    public static final String MESSAGE_LISTENER_ANNOTATION_CLASS = MessageListener.class.getCanonicalName();

    public static final String PROCESS_INTERFACE = poussecafe.domain.Process.class.getCanonicalName();

    public static final String PRODUCES_EVENT_ANNOTATION_CLASS = ProducesEvent.class.getCanonicalName();

    public static final String FACTORY_CLASS = AggregateFactory.class.getCanonicalName();

    public static final String DEPRECATED_FACTORY_CLASS = Factory.class.getCanonicalName();

    public static final String REPOSITORY_CLASS = AggregateRepository.class.getCanonicalName();

    public static final String DEPRECATED_REPOSITORY_CLASS = Repository.class.getCanonicalName();

    public static final String MESSAGE_CLASS = Message.class.getCanonicalName();

    public static final String DOMAIN_EVENT_INTERFACE = DomainEvent.class.getCanonicalName();

    public static final String COMMAND_INTERFACE = Command.class.getCanonicalName();

    public static final String AGGREGATE_ANNOTATION_CLASS = Aggregate.class.getCanonicalName();

    public static final String MESSAGE_IMPLEMENTATION_ANNOTATION_CLASS = MessageImplementation.class.getCanonicalName();

    public static final String ABSTRACT_MESSAGE_ANNOTATION_CLASS = AbstractMessage.class.getCanonicalName();

    public static final String ENTITY_CLASS = Entity.class.getCanonicalName();

    public static final String ENTITY_ATTRIBUTES_INTERFACE = EntityAttributes.class.getCanonicalName();

    public static final String DATA_IMPLEMENTATION_ANNOTATION_CLASS = DataImplementation.class.getCanonicalName();

    public static final String DATA_ACCESS_INTERFACE = EntityDataAccess.class.getCanonicalName();

    public static final String DATA_ACCESS_IMPLEMENTATION_ANNOTATION_CLASS = DataAccessImplementation.class.getCanonicalName();

    public static class Builder {

        public CompilationUnitResolver build() {
            requireNonNull(resolver.compilationUnit);
            requireNonNull(resolver.classResolver);
            resolver.init();
            return resolver;
        }

        private CompilationUnitResolver resolver = new CompilationUnitResolver();

        public Builder compilationUnit(CompilationUnit compilationUnit) {
            resolver.compilationUnit = compilationUnit;
            return this;
        }

        public Builder classResolver(ClassResolver classResolver) {
            resolver.classResolver = classResolver;
            return this;
        }
    }

    private CompilationUnitResolver() {

    }
}
