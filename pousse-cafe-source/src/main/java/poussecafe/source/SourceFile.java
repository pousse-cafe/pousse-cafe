package poussecafe.source;

import java.nio.file.Path;
import org.eclipse.jdt.core.dom.CompilationUnit;

import static java.util.Objects.requireNonNull;

public class SourceFile {

    public Path path() {
        return path;
    }

    private Path path;

    public CompilationUnit tree() {
        return tree;
    }

    private CompilationUnit tree;

    public static class Builder {

        public SourceFile build() {
            requireNonNull(sourceFileTree.path);
            requireNonNull(sourceFileTree.tree);
            return sourceFileTree;
        }

        private SourceFile sourceFileTree = new SourceFile();

        public Builder path(Path path) {
            sourceFileTree.path = path;
            return this;
        }

        public Builder tree(CompilationUnit tree) {
            sourceFileTree.tree = tree;
            return this;
        }
    }
}
