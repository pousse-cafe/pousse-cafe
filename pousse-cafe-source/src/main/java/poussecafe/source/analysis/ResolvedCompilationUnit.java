package poussecafe.source.analysis;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;
import poussecafe.source.SourceFile;
import poussecafe.source.validation.SourceFileLine;

public class ResolvedCompilationUnit {

    private Resolver resolver;

    public Resolver resolver() {
        return resolver;
    }

    public CompilationUnit compilationUnit() {
        return sourceFile.tree();
    }

    public String packageName() {
        return compilationUnit().getPackage().getName().getFullyQualifiedName();
    }

    public SourceFileLine sourceFileLine(ASTNode node) {
        return new SourceFileLine.Builder()
                .sourceFile(sourceFile)
                .line(lineNumber(node))
                .build();
    }

    public int lineNumber(ASTNode node) {
        return compilationUnit().getLineNumber(node.getStartPosition());
    }

    public SourceFile sourceFile() {
        return sourceFile;
    }

    private SourceFile sourceFile;

    public static class Builder {

        private ResolvedCompilationUnit resolvedCompilationUnit = new ResolvedCompilationUnit();

        public ResolvedCompilationUnit build() {
            return resolvedCompilationUnit;
        }

        public Builder withResolver(Resolver resolver) {
            resolvedCompilationUnit.resolver = resolver;
            return this;
        }

        public Builder withSourceFile(SourceFile sourceFile) {
            resolvedCompilationUnit.sourceFile = sourceFile;
            return this;
        }
    }

    private ResolvedCompilationUnit() {

    }
}
