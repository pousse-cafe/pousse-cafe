package poussecafe.source.generation.tools;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;

import static java.util.Objects.requireNonNull;

public class SingleMemberAnnotationEditor implements AnnotationEditor {

    public void setValue(Expression value) {
        Object currentValue = nodeRewrite.get(SingleMemberAnnotation.VALUE_PROPERTY);
        if(currentValue != null && !currentValue.toString().equals(value.toString())) {
            nodeRewrite.set(SingleMemberAnnotation.VALUE_PROPERTY, value);
        }
    }

    public Expression getValue() {
        return (Expression) nodeRewrite.get(SingleMemberAnnotation.VALUE_PROPERTY);
    }

    public SingleMemberAnnotationEditor(NodeRewrite nodeRewrite) {
        requireNonNull(nodeRewrite);
        this.nodeRewrite = nodeRewrite;
    }

    private NodeRewrite nodeRewrite;

    public NodeRewrite rewrite() {
        return nodeRewrite;
    }
}
