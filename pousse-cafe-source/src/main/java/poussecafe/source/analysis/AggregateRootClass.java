package poussecafe.source.analysis;

import java.util.Optional;
import poussecafe.source.generation.NamingConventions;

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
        if(isInnerClass()) {
            return name.resolvedClass().declaringClass().orElseThrow().name().simple();
        } else {
            return NamingConventions.aggregateNameFromSimpleRootName(name.simpleName());
        }
    }

    public boolean isInnerClass() {
        return name.resolvedClass().declaringClass().isPresent();
    }
}
