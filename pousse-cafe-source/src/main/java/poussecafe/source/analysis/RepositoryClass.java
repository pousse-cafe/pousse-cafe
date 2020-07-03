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
    }

    private ResolvedTypeName aggregateName;

    public ResolvedTypeName aggregateName() {
        return aggregateName;
    }
}
