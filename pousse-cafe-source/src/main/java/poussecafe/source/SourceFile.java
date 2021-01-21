package poussecafe.source;

import org.eclipse.jdt.core.dom.CompilationUnit;

import static java.util.Objects.requireNonNull;

public class SourceFile {

    public Source source() {
        return source;
    }

    private Source source;

    public String id() {
        return source.id();
    }

    public CompilationUnit tree() {
        return tree;
    }

    private CompilationUnit tree;

    public static class Builder {

        public SourceFile build() {
            requireNonNull(sourceFileTree.source);
            requireNonNull(sourceFileTree.tree);
            return sourceFileTree;
        }

        private SourceFile sourceFileTree = new SourceFile();

        public Builder source(Source source) {
            sourceFileTree.source = source;
            return this;
        }

        public Builder tree(CompilationUnit tree) {
            sourceFileTree.tree = tree;
            return this;
        }
    }

    private SourceFile() {

    }

    @Override
    public String toString() {
        return source.id();
    }
}
