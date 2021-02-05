package poussecafe.source.analysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import poussecafe.domain.Module;
import poussecafe.domain.Repository;
import poussecafe.environment.AggregateMessageListenerRunner;
import poussecafe.messaging.Message;
import poussecafe.runtime.Command;

import static java.util.Objects.requireNonNull;

@SuppressWarnings("deprecation")
public class CompilationUnitResolver implements Resolver {

    private void init() { // NOSONAR
        registerCompilationUnitTypes();
    }

    private CompilationUnit compilationUnit;

    public CompilationUnit compilationUnit() {
        return compilationUnit;
    }

    private ClassResolver classResolver;

    @Override
    public ClassResolver classResolver() {
        return classResolver;
    }

    private ResolutionException newResolutionException(String className) {
        return new ResolutionException("Unable to load class " + className);
    }

    private void registerCompilationUnitTypes() {
        var type = compilationUnit.types().get(0);
        if(type instanceof TypeDeclaration) {
            var rootType = (TypeDeclaration) type;
            var rootTypeSimpleName = rootType.getName().getIdentifier();
            var packageName = compilationUnit.getPackage().getName().getFullyQualifiedName();
            var rootTypeName = new Name(packageName, rootTypeSimpleName);
            resolvedTypeNames.put(rootTypeSimpleName,
                    new LazyResolver(rootTypeSimpleName, () -> Optional.of(resolveFullyQualifiedName(rootTypeName))));
            registerInnerClasses(rootType);
        }
    }

    private void registerInnerClasses(TypeDeclaration containerClass) {
        for(TypeDeclaration innerClass : containerClass.getTypes()) {
            var innerClassName = new Name(innerClass.getName().toString());
            var innerClassPath = new ArrayList<String>();
            innerClassPath.add(innerClassName.simple());
            registerClass(innerClassPath);
        }
    }

    private void registerClass(List<String> innerClassPath) {
        var packageName = compilationUnit.getPackage().getName().getFullyQualifiedName();
        var typeName = (AbstractTypeDeclaration) compilationUnit.types().get(0);
        var rootClassName = new Name(packageName, typeName.getName().getFullyQualifiedName());
        var innerClassName = innerClassPath.get(innerClassPath.size() - 1);
        resolvedTypeNames.put(innerClassName, new LazyResolver(innerClassName,
                        () -> classResolver.loadInnerClass(rootClassName, innerClassPath)));
    }

    private Map<String, LazyResolver> importedClasses = new HashMap<>();

    private Map<String, LazyResolver> resolvedTypeNames = new HashMap<>();

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
        var fullyQualifiedName = new Name(importDeclaration.getName().getFullyQualifiedName());
        registerClass(fullyQualifiedName, new LazyResolver(fullyQualifiedName.toString(),
                () -> Optional.of(resolveFullyQualifiedName(fullyQualifiedName))));
    }

    private void registerClass(Name className, LazyResolver resolver) {
        importedClasses.put(className.toString(), resolver);
        resolvedTypeNames.put(className.getIdentifier().toString(), resolver);
    }

    private ResolvedTypeName resolvedTypeName(ResolvedClass resolvedClass) {
        return new ResolvedTypeName.Builder()
            .withResolver(this)
            .withName(resolvedClass.name().getIdentifier())
            .withResolvedClass(resolvedClass)
            .build();
    }

    @Override
    public ResolvedTypeName resolve(Name name) {
        if(name.isQualifiedName()) {
            return resolvedTypeName(resolveFullyQualifiedName(name));
        } else {
            var simpleName = name.toString();
            var resolver = resolvedTypeNames.get(simpleName);
            Optional<ResolvedClass> resolvedClass;
            if(resolver != null) {
                resolvedClass = resolver.resolve();
            } else {
                resolvedClass = resolveSimpleName(name);
                if(resolvedClass.isPresent()) {
                    resolvedTypeNames.put(simpleName, new LazyResolver(simpleName, () -> resolvedClass));
                }
            }
            return resolvedClass.map(this::resolvedTypeName)
                    .orElseThrow(() -> newResolutionException(name.toString()));
        }
    }

    private ResolvedClass resolveFullyQualifiedName(Name name) {
        Optional<ResolvedClass> resolvedClass = tryNamingConventionBasedResolution(name);
        if(resolvedClass.isEmpty()) {
            logger.debug("Naming convention resolution failed for {}, falling back on generic method", name);
            resolvedClass = classResolver.loadClass(name);
        }
        if(resolvedClass.isPresent()) {
            return resolvedClass.get();
        } else {
            return resolvePartiallyQualifiedName(name);
        }
    }

    private Logger logger = LoggerFactory.getLogger(getClass());

    private Optional<ResolvedClass> tryNamingConventionBasedResolution(Name name) {
        var innerClassName = innerClassName(name);
        if(innerClassName.rootClassName().isQualifiedName()) {
            return classResolver.loadClass(innerClassName);
        } else if(!innerClassName.rootClassName().isQualifiedName()) {
            var rootClass = resolveSimpleName(innerClassName.rootClassName());
            if(rootClass.isPresent()) {
                if(innerClassName.isRootClassName()) {
                    return rootClass;
                } else {
                    return classResolver.locateInnerClass(rootClass.get(), innerClassName.innerClassPath());
                }
            } else {
                return Optional.empty();
            }
        } else {
            return Optional.empty();
        }
    }

    private SafeClassName innerClassName(Name name) {
        if(name.isQualifiedName()) {
            var segments = name.segments();
            var rootClassNameBuilder = new StringBuilder();
            int rootClassNameIndex;
            for(rootClassNameIndex = 0; rootClassNameIndex < segments.length; ++rootClassNameIndex) {
                var segment = segments[rootClassNameIndex];
                if(rootClassNameIndex > 0) {
                    rootClassNameBuilder.append('.');
                }
                rootClassNameBuilder.append(segment);
                if(Character.isUpperCase(segment.charAt(0))) {
                    break;
                }
            }
            var innerClassNameBuilder = new SafeClassName.Builder();
            innerClassNameBuilder.rootClassName(new Name(rootClassNameBuilder.toString()));
            for(int i = rootClassNameIndex + 1; i < segments.length; ++i) {
                var segment = segments[i];
                innerClassNameBuilder.appendPathElement(segment);
            }
            return innerClassNameBuilder.build();
        } else {
            return SafeClassName.ofRootClass(name);
        }
    }

    private ResolvedClass resolvePartiallyQualifiedName(Name name) {
        if(!name.isQualifiedName()) {
            throw new IllegalArgumentException();
        }
        var innerClassPath = new LinkedList<String>();
        var segments = name.segments();
        int i = segments.length - 1;
        if(importedClasses.containsKey(name.qualified())) { // Prevent infinite loop with single type imports
            innerClassPath.add(segments[i]);
            i = segments.length - 2;
        }
        for(; i >= 0; --i) {
            var simpleName = segments[i];
            var resolvedTypeName = resolveSimpleName(new Name(simpleName));
            if(resolvedTypeName.isPresent()) {
                return classResolver.loadInnerClass(new Name(resolvedTypeName.get().name().qualified()),
                            innerClassPath)
                        .orElseThrow(() -> newResolutionException(name.toString()));
            } else {
                innerClassPath.addFirst(simpleName);
            }
        }
        throw new ResolutionException("Unable to resolve " + name);
    }

    private Optional<ResolvedClass> resolveSimpleName(Name simpleName) {
        if(simpleName.isQualifiedName()) {
            throw new IllegalArgumentException();
        }

        var resolvedName = resolvedTypeNames.get(simpleName.toString());
        if(resolvedName != null) {
            return resolvedName.resolve();
        } else {
            return tryResolutionWithFrequentJavaLang(simpleName)
                    .or(() -> tryResolutionWithCompilationUnitPackage(simpleName))
                    .or(() -> tryResolutionWithImportedPackages(simpleName))
                    .or(() -> tryResolutionWithJavaLang(simpleName))
                    .or(() -> tryResolutionWithDefaultPackage(simpleName));
        }
    }

    private Optional<ResolvedClass> tryResolutionWithFrequentJavaLang(Name simpleName) {
        if(FREQUENT_JAVA_LANG.contains(simpleName.simple())) {
            return tryResolutionWithJavaLang(simpleName);
        } else {
            return Optional.empty();
        }
    }

    private static final Set<String> FREQUENT_JAVA_LANG = new HashSet<>();
    static {
        FREQUENT_JAVA_LANG.add("SuppressWarnings");
        FREQUENT_JAVA_LANG.add("Override");
    }

    private Optional<ResolvedClass> tryResolutionWithCompilationUnitPackage(Name simpleName) {
        return tryResolution(compilationUnitPackageName(), simpleName);
    }

    private Optional<ResolvedClass> tryResolutionWithImportedPackages(Name simpleName) {
        for(String importedPackage : importedPackages) {
            Optional<ResolvedClass> loadedClass = tryResolution(importedPackage, simpleName);
            if(loadedClass.isPresent()) {
                return Optional.of(loadedClass.get());
            }
        }
        return Optional.empty();
    }

    private String compilationUnitPackageName() {
        return compilationUnit.getPackage().getName().getFullyQualifiedName();
    }

    private Optional<ResolvedClass> tryResolutionWithJavaLang(Name simpleName) {
        return tryResolution("java.lang", simpleName);
    }

    private Optional<ResolvedClass> tryResolutionWithDefaultPackage(Name simpleName) {
        return tryResolution("", simpleName);
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

    public static final String AGGREGATE_MESSAGE_LISTENER_RUNNER_INTERFACE = AggregateMessageListenerRunner.class.getCanonicalName();

    public static final String MODULE_INTERFACE = Module.class.getCanonicalName();

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
