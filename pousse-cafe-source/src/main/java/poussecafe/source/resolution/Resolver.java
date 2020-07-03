package poussecafe.source.resolution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import poussecafe.discovery.MessageListener;
import poussecafe.domain.AggregateRoot;

import static java.util.Collections.emptySet;

public class Resolver {

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
        Set<Class<?>> monitoredClasses = MONITORED_PACKAGES.computeIfAbsent(packageName.getFullyQualifiedName(),
                key -> emptySet());
        monitoredClasses.stream().forEach(this::tryClassImport);
    }

    private void tryRegisterSingleTypeImport(ImportDeclaration importDeclaration) {
        Name importName = importDeclaration.getName();
        for(Class<?> expectedClass : new ArrayList<>(notImported)) {
            if(importName.getFullyQualifiedName().equals(expectedClass.getCanonicalName())) {
                tryClassImport(expectedClass);
            }
        }
    }

    private void tryClassImport(Class<?> expectedClass) {
        if(notImported.contains(expectedClass)) {
            imported.add(expectedClass);
            notImported.remove(expectedClass);
        }
    }

    private Set<Class<?>> notImported = new HashSet<>(MONITORED_CLASSES);

    private Set<Class<?>> imported = new HashSet<>();

    public boolean hasImport(Class<?> expectedClass) {
        return imported.contains(expectedClass);
    }

    public ResolvedTypeName resolve(Name name) {
        return new ResolvedTypeName.Builder()
                .withImports(this)
                .withName(name)
                .build();
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

    private static final Set<Class<?>> MONITORED_CLASSES = new HashSet<>();
    static {
        MONITORED_CLASSES.add(AggregateRoot.class);
        MONITORED_CLASSES.add(MessageListener.class);
        MONITORED_CLASSES.add(poussecafe.domain.Process.class);
    }

    private static final Map<String, Set<Class<?>>> MONITORED_PACKAGES = new HashMap<>();
    static {
        MONITORED_CLASSES.stream().forEach(monitoredClass -> MONITORED_PACKAGES
                .computeIfAbsent(monitoredClass.getPackageName(), key -> new HashSet<>())
                .add(monitoredClass));
    }
}
