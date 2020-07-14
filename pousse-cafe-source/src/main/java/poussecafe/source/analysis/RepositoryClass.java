package poussecafe.source.analysis;

import java.util.Optional;

public class RepositoryClass {

    public static boolean isRepository(ResolvedTypeDeclaration resolvedTypeDeclaration) {
        Optional<ResolvedTypeName> superclassType = resolvedTypeDeclaration.superclass();
        return superclassType.isPresent()
                && superclassType.get().isClass(Resolver.REPOSITORY_CLASS);
    }

    public RepositoryClass(ResolvedTypeDeclaration resolvedTypeDeclaration) {
        if(!isRepository(resolvedTypeDeclaration)) {
            throw new IllegalArgumentException();
        }
        aggregateName = resolvedTypeDeclaration.typeParameter(0);
        className = resolvedTypeDeclaration.name();
    }

    private ResolvedTypeName aggregateName;

    public ResolvedTypeName aggregateName() {
        return aggregateName;
    }

    private ResolvedTypeName className;

    public String simpleName() {
        return className.simpleName();
    }
}
