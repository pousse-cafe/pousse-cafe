package poussecafe.source.generation.tools;

import java.util.Optional;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jdt.core.dom.NormalAnnotation;

import static java.util.Objects.requireNonNull;

public class NormalAnnotationEditor implements AnnotationEditor {

    public void setAttribute(String name, Expression value) {
        Optional<MemberValuePair> attribute = findAttribute(name);
        if(attribute.isPresent()) {
            Object currentValue = rewrite.rewrite().get(attribute.get(), MemberValuePair.VALUE_PROPERTY);
            if(currentValue != null && !currentValue.toString().equals(value.toString())) {
                rewrite.rewrite().set(attribute.get(), MemberValuePair.VALUE_PROPERTY, value, null);
            }
        } else {
            rewrite.listRewrite(NormalAnnotation.VALUES_PROPERTY).insertLast(newAttribute(name, value), null);
        }
    }

    private Optional<MemberValuePair> findAttribute(String name) {
        var values = rewrite.listRewrite(NormalAnnotation.VALUES_PROPERTY).getRewrittenList();
        for(Object attributeValueObject : values) {
            MemberValuePair attributeValue = (MemberValuePair) attributeValueObject;
            if(attributeValue.getName().getIdentifier().equals(name)) {
                return Optional.of(attributeValue);
            }
        }
        return Optional.empty();
    }

    private MemberValuePair newAttribute(String name, Expression value) {
        var ast = rewrite.ast();
        var attribute = ast.newMemberValuePair();
        attribute.setName(ast.newSimpleName(name));
        attribute.setValue(value);
        return attribute;
    }

    public boolean hasAttribute(String name) {
        return findAttribute(name).isPresent();
    }

    public void removeAttribute(String name) {
        var attribute = findAttribute(name);
        if(attribute.isPresent()) {
            rewrite.listRewrite(NormalAnnotation.VALUES_PROPERTY).remove(attribute.get(), null);
        }
    }

    public Optional<Expression> getAttribute(String name) {
        Optional<MemberValuePair> attribute = findAttribute(name);
        return attribute.map(MemberValuePair::getValue);
    }

    public NormalAnnotationEditor(NodeRewrite rewrite) {
        requireNonNull(rewrite);
        this.rewrite = rewrite;
    }

    private NodeRewrite rewrite;
}
