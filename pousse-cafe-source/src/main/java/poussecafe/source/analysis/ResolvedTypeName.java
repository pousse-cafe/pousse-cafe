package poussecafe.source.analysis;

import static java.util.Objects.requireNonNull;

public class ResolvedTypeName {

    private Resolver resolver;

    private Name name;

    private Class<?> resolvedClass;

    public boolean isClass(Class<?> expectedClass) {
        return expectedClass.equals(resolvedClass);
    }

    public String simpleName() {
        return resolvedClass.getSimpleName();
    }

    public boolean instanceOf(Class<?> superType) {
        return superType.isAssignableFrom(resolvedClass);
    }

    public String qualifiedName() {
        return resolvedClass.getCanonicalName();
    }

    public String packageName() {
        return resolvedClass.getPackageName();
    }

    public static class Builder {

        private ResolvedTypeName resolved = new ResolvedTypeName();

        public Builder withResolver(Resolver resolver) {
            resolved.resolver = resolver;
            return this;
        }

        public Builder withName(Name name) {
            resolved.name = name;
            return this;
        }

        public Builder withResolvedClass(Class<?> resolvedClass) {
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
