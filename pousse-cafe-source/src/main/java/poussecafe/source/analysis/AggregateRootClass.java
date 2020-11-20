package poussecafe.source.analysis;

import java.util.Optional;

public class AggregateRootClass {

    public static boolean isAggregateRoot(ResolvedTypeDeclaration resolvedTypeDeclaration) {
        Optional<ResolvedTypeName> superclassType = resolvedTypeDeclaration.superclass();
        return superclassType.isPresent()
                && superclassType.get().isClass(CompilationUnitResolver.AGGREGATE_ROOT_CLASS);
    }

    public AggregateRootClass(ResolvedTypeDeclaration resolvedTypeDeclaration) {
        if(!isAggregateRoot(resolvedTypeDeclaration)) {
            throw new IllegalArgumentException();
        }
        name = resolvedTypeDeclaration.name();
    }

    private ResolvedTypeName name;

    public String aggregateName() {
        if(name.resolvedClass().getDeclaringClass() == null) {
            return name.simpleName();
        } else {
            return name.resolvedClass().getDeclaringClass().getSimpleName();
        }
    }
}
