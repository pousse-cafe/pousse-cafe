package poussecafe.source.validation;

import java.util.Set;
import poussecafe.discovery.ReflectionsWrapper;
import poussecafe.source.analysis.ClassLoaderClassResolver;
import poussecafe.source.analysis.ClassLoaderResolvedClass;
import poussecafe.source.analysis.ClassName;
import poussecafe.source.analysis.ResolvedClass;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toSet;

public class ReflectionsClassPathExplorer implements ClassPathExplorer {

    @Override
    public Set<ResolvedClass> getSubTypesOf(ClassName resolvedClassQualifiedName) {
        var resolvedClass = resolver.loadClass(resolvedClassQualifiedName).orElseThrow();
        var classLoaderResolvedClass = (ClassLoaderResolvedClass) resolvedClass;
        return reflections.getSubTypesOf(classLoaderResolvedClass.classObject()).stream()
                .map(classObject -> new ClassLoaderResolvedClass.Builder()
                        .classObject(classObject)
                        .classResolver((ClassLoaderClassResolver) classLoaderResolvedClass.resolver())
                        .build())
                .collect(toSet());
    }

    private ClassLoaderClassResolver resolver;

    private ReflectionsWrapper reflections;

    public static class Builder {

        public ReflectionsClassPathExplorer build() {
            requireNonNull(explorer.resolver);
            requireNonNull(explorer.reflections);
            return explorer;
        }

        private ReflectionsClassPathExplorer explorer = new ReflectionsClassPathExplorer();

        public Builder resolver(ClassLoaderClassResolver resolver) {
            explorer.resolver = resolver;
            return this;
        }

        public Builder reflections(ReflectionsWrapper reflections) {
            explorer.reflections = reflections;
            return this;
        }
    }

    private ReflectionsClassPathExplorer() {

    }
}
