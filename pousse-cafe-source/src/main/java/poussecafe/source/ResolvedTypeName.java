package poussecafe.source;

import org.eclipse.jdt.core.dom.Name;

import static java.util.Objects.requireNonNull;

public class ResolvedTypeName {

    private Imports imports;

    private Name name;

    public boolean isClass(Class<?> expectedClass) {
        return isClassGivenUnqualifiedName(expectedClass)
                || isClassGivenQualifiedName(expectedClass);
    }

    private boolean isClassGivenUnqualifiedName(Class<?> expectedClass) {
        return !name.isQualifiedName()
                && imports.hasImport(expectedClass)
                && name.toString().equals(expectedClass.getSimpleName());
    }

    private boolean isClassGivenQualifiedName(Class<?> expectedClass) {
        return name.isQualifiedName()
                && name.getFullyQualifiedName().equals(expectedClass.getCanonicalName());
    }

    public static class Builder {

        private ResolvedTypeName resolved = new ResolvedTypeName();

        public Builder withImports(Imports imports) {
            resolved.imports = imports;
            return this;
        }

        public Builder withName(Name name) {
            resolved.name = name;
            return this;
        }

        public ResolvedTypeName build() {
            requireNonNull(resolved.imports);
            requireNonNull(resolved.name);
            return resolved;
        }
    }

    private ResolvedTypeName() {

    }
}
