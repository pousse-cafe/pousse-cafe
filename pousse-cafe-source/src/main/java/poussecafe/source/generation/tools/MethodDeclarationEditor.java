package poussecafe.source.generation.tools;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import poussecafe.source.analysis.Name;

import static java.util.Objects.requireNonNull;

public class MethodDeclarationEditor {

    public void setName(String name) {
        rewrite.set(MethodDeclaration.NAME_PROPERTY, rewrite.ast().newSimpleName(name));
    }

    public ModifiersEditor modifiers() {
        return new ModifiersEditor(rewrite, MethodDeclaration.MODIFIERS2_PROPERTY);
    }

    public void setBody(Block body) {
        rewrite.set(MethodDeclaration.BODY_PROPERTY, body);
    }

    public void clearParameters() {
        ListRewrite parametersRewrite = rewrite.listRewrite(MethodDeclaration.PARAMETERS_PROPERTY);
        for(Object parameter : methodDeclaration.parameters()) {
            parametersRewrite.remove((ASTNode) parameter, null);
        }
    }

    private MethodDeclaration methodDeclaration;

    public void addParameter(Name typeName, String parameterName) {
        var constructorParameter = new AstWrapper(rewrite.ast()).newSimpleMethodParameter(typeName.toString(), parameterName);
        rewrite.listRewrite(MethodDeclaration.PARAMETERS_PROPERTY).insertLast(constructorParameter, null);
    }

    public void setReturnType(Type returnType) {
        rewrite.set(MethodDeclaration.RETURN_TYPE2_PROPERTY, returnType);
    }

    public MethodDeclarationEditor(NodeRewrite rewrite, boolean isNewNode) {
        requireNonNull(rewrite);
        this.rewrite = rewrite;

        this.isNewNode = isNewNode;

        methodDeclaration = (MethodDeclaration) rewrite.node();
    }

    private NodeRewrite rewrite;

    public boolean isNewNode() {
        return isNewNode;
    }

    private boolean isNewNode;

    public AstWrapper ast() {
        return new AstWrapper(rewrite.node().getAST());
    }
}