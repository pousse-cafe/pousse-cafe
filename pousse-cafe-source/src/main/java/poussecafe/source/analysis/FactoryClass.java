package poussecafe.source.analysis;

import java.util.Optional;

public class FactoryClass {

    public static boolean isFactory(ResolvedTypeDeclaration resolvedTypeDeclaration) {
        Optional<ResolvedTypeName> superclassType = resolvedTypeDeclaration.superclass();
        return superclassType.isPresent()
                && superclassType.get().isClass(Resolver.FACTORY_CLASS);
    }

    public FactoryClass(ResolvedTypeDeclaration resolvedTypeDeclaration) {
        if(!isFactory(resolvedTypeDeclaration)) {
            throw new IllegalArgumentException();
        }

        aggregateName = resolvedTypeDeclaration.typeParameter(1);
    }

    private ResolvedTypeName aggregateName;

    public ResolvedTypeName aggregateName() {
        return aggregateName;
    }
}
