package poussecafe.source.analysis;

public class DataAccessInterface {

    public static boolean isDataAccess(ResolvedTypeDeclaration type) {
        return type.name().instanceOf(CompilationUnitResolver.DATA_ACCESS_INTERFACE)
                && type.typeDeclaration().isInterface();
    }

    public SafeClassName className() {
        return type.unresolvedName();
    }

    private ResolvedTypeDeclaration type;

    public DataAccessInterface(ResolvedTypeDeclaration typeName) {
        type = typeName;
    }
}
