package poussecafe.source.analysis;

import java.lang.annotation.Annotation;
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
import poussecafe.discovery.Aggregate;
import poussecafe.discovery.MessageListener;
import poussecafe.discovery.ProducesEvent;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.DomainEvent;
import poussecafe.domain.Factory;
import poussecafe.domain.Repository;
import poussecafe.messaging.Message;
import poussecafe.runtime.Command;

import static java.util.Objects.requireNonNull;

public class CompilationUnitResolver implements Resolver {

    public CompilationUnitResolver(CompilationUnit compilationUnit) {
        requireNonNull(compilationUnit);
        this.compilationUnit = compilationUnit;
        loadCompilationUnitClass();
        registerInnerClasses(compilationUnitClass);
    }

    private CompilationUnit compilationUnit;

    private Class<?> compilationUnitClass;

    private void loadCompilationUnitClass() {
        var packageName = compilationUnit.getPackage().getName().getFullyQualifiedName();
        var typeName = (AbstractTypeDeclaration) compilationUnit.types().get(0);
        compilationUnitClass = classResolver.loadClass(new Name(packageName + "." + typeName.getName().getFullyQualifiedName())).orElseThrow();
    }

    private ClassResolver classResolver = new ClassResolver();

    private void registerInnerClasses(Class<?> containerClass) {
        for(Class<?> innerClass : containerClass.getDeclaredClasses()) {
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
        registerClass(classResolver.loadClass(fullyQualifiedName).orElseThrow());
    }

    private void registerClass(Class<?> importedClass) {
        importedClasses.put(importedClass.getCanonicalName(), importedClass);
        resolvedTypeNames.put(importedClass.getSimpleName(), resolvedTypeName(importedClass));
    }

    private Map<String, Class<?>> importedClasses = new HashMap<>();

    private ResolvedTypeName resolvedTypeName(Class<?> resolvedClass) {
        return new ResolvedTypeName.Builder()
            .withResolver(this)
            .withName(new Name(resolvedClass.getSimpleName()))
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
                    .orElseThrow(() -> new ResolutionException("Unable to resolve " + name)));
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
                        innerClassPath).orElseThrow();
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
            Optional<Class<?>> loadedClass = tryResolution(importedPackage, simpleName);
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

    private Optional<Class<?>> tryResolution(String packageName, Name simpleName) {
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

    public static final Class<?> AGGREGATE_ROOT_CLASS = AggregateRoot.class;

    public static final Class<? extends Annotation> MESSAGE_LISTENER_ANNOTATION_CLASS = MessageListener.class;

    public static final Class<?> PROCESS_INTERFACE = poussecafe.domain.Process.class;

    public static final Class<? extends Annotation> PRODUCES_EVENT_ANNOTATION_CLASS = ProducesEvent.class;

    public static final Class<?> FACTORY_CLASS = Factory.class;

    public static final Class<?> REPOSITORY_CLASS = Repository.class;

    public static final Class<?> MESSAGE_CLASS = Message.class;

    public static final Class<?> DOMAIN_EVENT_INTERFACE = DomainEvent.class;

    public static final Class<?> COMMAND_INTERFACE = Command.class;

    public static final Class<? extends Annotation> AGGREGATE_ANNOTATION_CLASS = Aggregate.class;
}
