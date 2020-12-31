package poussecafe.source;

import org.eclipse.jdt.core.dom.CompilationUnit;

import static java.util.Objects.requireNonNull;

public class SourceFile {

    public String id() {
        return id;
    }

    private String id;

    public CompilationUnit tree() {
        return tree;
    }

    private CompilationUnit tree;

    public static class Builder {

        public SourceFile build() {
            requireNonNull(sourceFileTree.id);
            requireNonNull(sourceFileTree.tree);
            return sourceFileTree;
        }

        private SourceFile sourceFileTree = new SourceFile();

        public Builder id(String id) {
            sourceFileTree.id = id;
            return this;
        }

        public Builder tree(CompilationUnit tree) {
            sourceFileTree.tree = tree;
            return this;
        }
    }

    @Override
    public String toString() {
        return id;
    }
}
