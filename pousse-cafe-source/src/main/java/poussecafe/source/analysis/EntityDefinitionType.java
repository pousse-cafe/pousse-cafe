package poussecafe.source.analysis;

public class EntityDefinitionType {

    public static boolean isEntityDefinition(ResolvedTypeDeclaration type) {
        return type.name().instanceOf(CompilationUnitResolver.ENTITY_CLASS);
    }

    public String name() {
        if(typeName.name().instanceOf(CompilationUnitResolver.AGGREGATE_ROOT_CLASS)
                && typeName.isInnerClass()) {
            return typeName.declaringType().name().simpleName();
        } else {
            return typeName.name().simpleName();
        }
    }

    private ResolvedTypeDeclaration typeName;

    public EntityDefinitionType(ResolvedTypeDeclaration typeName) {
        this.typeName = typeName;
    }
}
