package poussecafe.source.analysis;

public class EntityDefinitionType {

    public static boolean isEntityDefinition(ResolvedTypeDeclaration type) {
        return type.name().instanceOf(CompilationUnitResolver.ENTITY_CLASS);
    }

    public String name() {
        return typeName.name().simpleName();
    }

    private ResolvedTypeDeclaration typeName;

    public EntityDefinitionType(ResolvedTypeDeclaration typeName) {
        this.typeName = typeName;
    }
}
