package poussecafe.source.analysis;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;
import poussecafe.source.Source;
import poussecafe.source.validation.SourceLine;

import static java.util.Objects.requireNonNull;

public class ResolvedCompilationUnit {

    private Resolver resolver;

    public Resolver resolver() {
        return resolver;
    }

    public CompilationUnit compilationUnit() {
        return sourceFile.compilationUnit();
    }

    public String packageName() {
        return compilationUnit().getPackage().getName().getFullyQualifiedName();
    }

    public SourceLine sourceFileLine(ASTNode node) {
        return new SourceLine.Builder()
                .source(sourceFile)
                .line(lineNumber(node))
                .build();
    }

    public int lineNumber(ASTNode node) {
        return compilationUnit().getLineNumber(node.getStartPosition());
    }

    public Source sourceFile() {
        return sourceFile;
    }

    private Source sourceFile;

    public static class Builder {

        private ResolvedCompilationUnit resolvedCompilationUnit = new ResolvedCompilationUnit();

        public ResolvedCompilationUnit build() {
            requireNonNull(resolvedCompilationUnit.resolver);
            requireNonNull(resolvedCompilationUnit.sourceFile);
            return resolvedCompilationUnit;
        }

        public Builder withResolver(Resolver resolver) {
            resolvedCompilationUnit.resolver = resolver;
            return this;
        }

        public Builder withSourceFile(Source sourceFile) {
            resolvedCompilationUnit.sourceFile = sourceFile;
            return this;
        }
    }

    private ResolvedCompilationUnit() {

    }
}
