package poussecafe.source.generation;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ChildListPropertyDescriptor;
import org.eclipse.jdt.core.dom.ChildPropertyDescriptor;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

import static java.util.Objects.requireNonNull;

public class NodeRewrite {

    public AST ast() {
        return ast;
    }

    private AST ast;

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

    public void set(ChildPropertyDescriptor packageProperty, Object data) {
        rewrite.set(node, packageProperty, data, null);
    }

    public NodeRewrite(ASTNode node) {
        requireNonNull(node);
        this.node = node;

        ast = node.getAST();
        rewrite = ASTRewrite.create(ast);
    }
}
