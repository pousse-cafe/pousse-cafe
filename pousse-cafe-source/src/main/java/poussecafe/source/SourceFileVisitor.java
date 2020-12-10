package poussecafe.source;

@FunctionalInterface
public interface SourceFileVisitor {

    void visitFile(SourceFile sourceFile);
}
