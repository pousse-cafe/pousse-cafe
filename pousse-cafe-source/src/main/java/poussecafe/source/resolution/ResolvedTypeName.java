package poussecafe.source.resolution;

import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.QualifiedName;

import static java.util.Objects.requireNonNull;

public class ResolvedTypeName {

    private Resolver resolver;

    private Name name;

    public boolean isClass(Class<?> expectedClass) {
        return isClassGivenUnqualifiedName(expectedClass)
                || isClassGivenQualifiedName(expectedClass);
    }

    private boolean isClassGivenUnqualifiedName(Class<?> expectedClass) {
        return !name.isQualifiedName()
                && resolver.hasImport(expectedClass)
                && name.toString().equals(expectedClass.getSimpleName());
    }

    private boolean isClassGivenQualifiedName(Class<?> expectedClass) {
        return name.isQualifiedName()
                && name.getFullyQualifiedName().equals(expectedClass.getCanonicalName());
    }

    public String simpleName() {
        if(name.isSimpleName()) {
            return name.getFullyQualifiedName();
        } else {
            QualifiedName qualifiedProcessName = (QualifiedName) name;
            return qualifiedProcessName.getName().getIdentifier();
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

        public ResolvedTypeName build() {
            requireNonNull(resolved.resolver);
            requireNonNull(resolved.name);
            return resolved;
        }
    }

    private ResolvedTypeName() {

    }
}
