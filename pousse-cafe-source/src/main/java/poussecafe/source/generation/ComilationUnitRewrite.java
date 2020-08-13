package poussecafe.source.generation;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.TextEdit;

public class ComilationUnitRewrite extends NodeRewrite {

    public void rewrite(Document document) throws BadLocationException {
        TextEdit rewriterEdits = rewrite().rewriteAST(document, null);
        rewriterEdits.apply(document);
    }

    public CompilationUnit comilationUnit() {
        return (CompilationUnit) node();
    }

    public ComilationUnitRewrite(CompilationUnit unit) {
        super(ASTRewrite.create(unit.getAST()), unit);
    }
}
