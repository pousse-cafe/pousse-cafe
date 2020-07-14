package poussecafe.source.analysis;

import java.util.Optional;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.QualifiedName;

import static java.util.Objects.requireNonNull;

public class ResolvedTypeName {

    private Resolver resolver;

    private Name name;

    private Optional<Class<?>> resolvedClass = Optional.empty();

    public boolean isClass(Class<?> expectedClass) {
        resolvedOrElseThrow();
        return expectedClass.equals(resolvedClass.orElseThrow());
    }

    private void resolvedOrElseThrow() {
        if(resolvedClass.isEmpty()) {
            throw new IllegalStateException("Type " + name + " could not be resolved");
        }
    }

    public String simpleName() {
        if(name.isSimpleName()) {
            return name.getFullyQualifiedName();
        } else {
            QualifiedName qualifiedProcessName = (QualifiedName) name;
            return qualifiedProcessName.getName().getIdentifier();
        }
    }

    public boolean instanceOf(Class<?> superType) {
        resolvedOrElseThrow();
        return superType.isAssignableFrom(resolvedClass.orElseThrow());
    }

    public String qualifiedName() {
        if(name.isQualifiedName()) {
            return name.getFullyQualifiedName();
        } else {
            resolvedOrElseThrow();
            return resolvedClass.orElseThrow().getCanonicalName();
        }
    }

    public static class Builder {

        private ResolvedTypeName resolved = new ResolvedTypeName();

        public Builder withImports(Resolver resolver) {
            resolved.resolver = resolver;
            return this;
        }

        public Builder withName(Name name) {
            resolved.name = name;
            return this;
        }

        public Builder withResolvedClass(Optional<Class<?>> resolvedClass) {
            resolved.resolvedClass = resolvedClass;
            return this;
        }

        public ResolvedTypeName build() {
            requireNonNull(resolved.resolver);
            requireNonNull(resolved.name);
            requireNonNull(resolved.resolvedClass);
            return resolved;
        }
    }

    private ResolvedTypeName() {

    }
}
