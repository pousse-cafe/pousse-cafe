package poussecafe.source.analysis;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import poussecafe.discovery.MessageListener;
import poussecafe.discovery.ProducesEvent;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.DomainEvent;
import poussecafe.domain.Factory;
import poussecafe.domain.Repository;
import poussecafe.messaging.Message;
import poussecafe.runtime.Command;

import static java.util.Objects.requireNonNull;

public class Resolver {

    public Resolver(CompilationUnit compilationUnit) {
        requireNonNull(compilationUnit);
        this.compilationUnit = compilationUnit;
    }

    private CompilationUnit compilationUnit;

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
        Name packageName = importDeclaration.getName();
        importedPackages.add(packageName.getFullyQualifiedName());
    }

    private List<String> importedPackages = new ArrayList<>();

    private void tryRegisterSingleTypeImport(ImportDeclaration importDeclaration) {
        QualifiedName importName = (QualifiedName) importDeclaration.getName();
        String fullyQualifiedName = importName.getFullyQualifiedName();
        loadClass(fullyQualifiedName).ifPresent(this::registerImportedClass);
    }

    private void registerImportedClass(Class<?> importedClass) {
        importedClasses.put(importedClass.getCanonicalName(), importedClass);
        resolvedNames.put(importedClass.getSimpleName(), importedClass);
    }

    private Optional<Class<?>> loadClass(String importedClassName) {
        try {
            return Optional.of(getClass().getClassLoader().loadClass(importedClassName));
        } catch (ClassNotFoundException e) {
            return Optional.empty();
        }
    }

    private Map<String, Class<?>> importedClasses = new HashMap<>();

    public ResolvedTypeName resolve(Name name) {
        Optional<Class<?>> resolvedClass = resolveClass(name);
        return new ResolvedTypeName.Builder()
                .withImports(this)
                .withName(name)
                .withResolvedClass(resolvedClass)
                .build();
    }

    private Optional<Class<?>> resolveClass(Name name) {
        if(name.isQualifiedName()) {
            String resolvedClassName = name.getFullyQualifiedName();
            return loadClass(resolvedClassName);
        } else {
            String simpleName = name.getFullyQualifiedName();
            return Optional.ofNullable(resolvedNames.computeIfAbsent(simpleName, this::resolveName));
        }
    }

    private Map<String, Class<?>> resolvedNames = new HashMap<>();

    private Class<?> resolveName(String simpleName) {
        for(String importedPackage : importedPackages) {
            Optional<Class<?>> loadedClass = tryResolution(importedPackage, simpleName);
            if(loadedClass.isPresent()) {
                return loadedClass.get();
            }
        }
        return tryResolution(compilationUnitPackageName(), simpleName).orElse(null);
    }

    private String compilationUnitPackageName() {
        return compilationUnit.getPackage().getName().getFullyQualifiedName();
    }

    private Optional<Class<?>> tryResolution(String packageName, String simpleName) {
        String candidateQualifiedName = packageName + "." + simpleName;
        return loadClass(candidateQualifiedName);
    }

    public ResolvedMethod resolve(MethodDeclaration method) {
        return new ResolvedMethod.Builder()
                .withImports(this)
                .withDeclaration(method)
                .build();
    }

    public ResolvedTypeDeclaration resolve(TypeDeclaration type) {
        return new ResolvedTypeDeclaration.Builder()
                .withImports(this)
                .withDeclaration(type)
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
}
