package poussecafe.source.generation.tools;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.TextEdit;

public class CompilationUnitRewrite extends NodeRewrite {

    public TextEdit rewrite(Document document) throws BadLocationException {
        TextEdit rewriterEdits = rewrite().rewriteAST(document, null);
        rewriterEdits.apply(document);
        return rewriterEdits;
    }

    public CompilationUnit compilationUnit() {
        return (CompilationUnit) node();
    }

    public CompilationUnitRewrite(CompilationUnit unit) {
        super(ASTRewrite.create(unit.getAST()), unit);
    }
}
