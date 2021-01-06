package poussecafe.source.analysis;

import poussecafe.source.generation.NamingConventions;

public class RepositoryClass {

    public static boolean isRepository(ResolvedTypeDeclaration resolvedTypeDeclaration) {
        return isInstanceOfRepository(resolvedTypeDeclaration)
                    || isInstanceOfDeprecatedRepository(resolvedTypeDeclaration);
    }

    private static boolean isInstanceOfDeprecatedRepository(ResolvedTypeDeclaration resolvedTypeDeclaration) {
        var superclassType = resolvedTypeDeclaration.superclass();
        return superclassType.isPresent()
                && superclassType.get().isClass(CompilationUnitResolver.DEPRECATED_REPOSITORY_CLASS);
    }

    private static boolean isInstanceOfRepository(ResolvedTypeDeclaration resolvedTypeDeclaration) {
        var superclassType = resolvedTypeDeclaration.superclass();
        return superclassType.isPresent()
                && superclassType.get().isClass(CompilationUnitResolver.REPOSITORY_CLASS);
    }

    public RepositoryClass(ResolvedTypeDeclaration resolvedTypeDeclaration) {
        if(!isRepository(resolvedTypeDeclaration)) {
            throw new IllegalArgumentException();
        }
        if(isInstanceOfRepository(resolvedTypeDeclaration)) {
            aggregateRootName = resolvedTypeDeclaration.superclassType().orElseThrow().typeParameters().get(1).toTypeName();
        } else {
            aggregateRootName = resolvedTypeDeclaration.superclassType().orElseThrow().typeParameters().get(0).toTypeName();
        }
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
