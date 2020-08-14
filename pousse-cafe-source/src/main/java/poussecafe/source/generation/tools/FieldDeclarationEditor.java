package poussecafe.source.generation.tools;

import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.Type;

import static java.util.Objects.requireNonNull;

public class FieldDeclarationEditor {

    public ModifiersEditor modifiers() {
        return new ModifiersEditor(nodeRewrite, FieldDeclaration.MODIFIERS2_PROPERTY);
    }

    public void setType(Type type) {
        nodeRewrite.set(FieldDeclaration.TYPE_PROPERTY, type);
    }

    public FieldDeclarationEditor(NodeRewrite nodeRewrite) {
        requireNonNull(nodeRewrite);
        this.nodeRewrite = nodeRewrite;
    }

    private NodeRewrite nodeRewrite;
}
