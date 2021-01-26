package poussecafe.source.analysis;

public interface ResolvedCompilationUnitVisitor {

    default boolean visit(ResolvedCompilationUnit unit) {
        return false;
    }

    default boolean visit(ResolvedTypeDeclaration type) {
        return false;
    }

    default void endVisit(ResolvedTypeDeclaration type) {

    }

    default boolean visit(ResolvedMethod method) {
        return false;
    }

    default boolean foundContent() {
        return false;
    }
}
