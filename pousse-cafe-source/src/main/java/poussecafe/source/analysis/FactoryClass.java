package poussecafe.source.analysis;

import java.util.Optional;

public class FactoryClass {

    public static boolean isFactory(ResolvedTypeDeclaration resolvedTypeDeclaration) {
        Optional<ResolvedTypeName> superclassType = resolvedTypeDeclaration.superclass();
        return superclassType.isPresent()
                && (superclassType.get().isClass(CompilationUnitResolver.FACTORY_CLASS)
                        || superclassType.get().isClass(CompilationUnitResolver.DEPRECATED_FACTORY_CLASS));
    }

    public FactoryClass(ResolvedTypeDeclaration resolvedTypeDeclaration) {
        if(!isFactory(resolvedTypeDeclaration)) {
            throw new IllegalArgumentException();
        }
        aggregateName = resolvedTypeDeclaration.typeParameter(1);
        className = resolvedTypeDeclaration.name();
    }

    private ResolvedTypeName aggregateName;

    public String aggregateName() {
        if(aggregateName.resolvedClass().getDeclaringClass() == null) {
            return aggregateName.simpleName();
        } else {
            return aggregateName.resolvedClass().getDeclaringClass().getSimpleName();
        }
    }

    private ResolvedTypeName className;

    public String simpleName() {
        return className.simpleName();
    }
}
