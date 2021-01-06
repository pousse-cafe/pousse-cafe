package poussecafe.source.analysis;

import poussecafe.source.generation.NamingConventions;

public class FactoryClass {

    public static boolean isFactory(ResolvedTypeDeclaration resolvedTypeDeclaration) {
        var superclassType = resolvedTypeDeclaration.superclass();
        return superclassType.isPresent()
                && (superclassType.get().isClass(CompilationUnitResolver.FACTORY_CLASS)
                        || superclassType.get().isClass(CompilationUnitResolver.DEPRECATED_FACTORY_CLASS));
    }

    public FactoryClass(ResolvedTypeDeclaration resolvedTypeDeclaration) {
        if(!isFactory(resolvedTypeDeclaration)) {
            throw new IllegalArgumentException();
        }
        aggregateRootName = resolvedTypeDeclaration.superclassType().typeParameters().get(1).toTypeName();
        className = resolvedTypeDeclaration.name();
    }

    private ResolvedTypeName aggregateRootName;

    public String aggregateName() {
        var declaringClass = aggregateRootName.resolvedClass().declaringClass();
        if(declaringClass.isEmpty()) {
            return NamingConventions.aggregateNameFromSimpleRootName(aggregateRootName.simpleName());
        } else {
            return declaringClass.get().name().simple();
        }
    }

    private ResolvedTypeName className;

    public String simpleName() {
        return className.simpleName();
    }
}
