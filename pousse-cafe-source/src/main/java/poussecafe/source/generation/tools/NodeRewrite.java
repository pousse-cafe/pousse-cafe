package poussecafe.source.generation.tools;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ChildListPropertyDescriptor;
import org.eclipse.jdt.core.dom.ChildPropertyDescriptor;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.StructuralPropertyDescriptor;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

import static java.util.Objects.requireNonNull;

public class NodeRewrite {

    public AST ast() {
        return rewrite.getAST();
    }

    public ASTRewrite rewrite() {
        return rewrite;
    }

    private ASTRewrite rewrite;

    public ASTNode node() {
        return node;
    }

    private ASTNode node;

    public ListRewrite listRewrite(ChildListPropertyDescriptor property) {
        return rewrite.getListRewrite(node, property);
    }

    public void set(StructuralPropertyDescriptor packageProperty, Object data) {
        rewrite.set(node, packageProperty, data, null);
    }

    public Object get(ChildPropertyDescriptor property) {
        return rewrite.get(node, property);
    }

    public Statement lineCommentStatement(String comment) {
        return (Statement) rewrite.createStringPlaceholder("\n// " + comment + "\n",
                ASTNode.EMPTY_STATEMENT);
    }

    public NodeRewrite(ASTRewrite rewrite, ASTNode node) {
        requireNonNull(rewrite);
        this.rewrite = rewrite;

        requireNonNull(node);
        this.node = node;
    }
}
