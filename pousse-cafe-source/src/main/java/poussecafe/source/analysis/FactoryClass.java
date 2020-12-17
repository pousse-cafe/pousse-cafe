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
        Optional<ResolvedClass> declaringClass = aggregateName.resolvedClass().declaringClass();
        if(declaringClass.isEmpty()) {
            return aggregateName.simpleName();
        } else {
            return declaringClass.get().name().simple();
        }
    }

    private ResolvedTypeName className;

    public String simpleName() {
        return className.simpleName();
    }
}
