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
            rewrite.rewrite().set(attribute.get(), MemberValuePair.VALUE_PROPERTY, value, null);
        } else {
            rewrite.listRewrite(NormalAnnotation.VALUES_PROPERTY).insertLast(newAttribute(name, value), null);
        }
    }

    private Optional<MemberValuePair> findAttribute(String name) {
        for(Object modifierObject : annotation.values()) {
            MemberValuePair value = (MemberValuePair) modifierObject;
            if(value.getName().getIdentifier().equals(name)) {
                return Optional.of(value);
            }
        }
        return Optional.empty();
    }

    private NormalAnnotation annotation;

    private MemberValuePair newAttribute(String name, Expression value) {
        var ast = rewrite.ast();
        var attribute = ast.newMemberValuePair();
        attribute.setName(ast.newSimpleName(name));
        attribute.setValue(value);
        return attribute;
    }

    public NormalAnnotationEditor(NodeRewrite rewrite) {
        requireNonNull(rewrite);
        this.rewrite = rewrite;

        annotation = (NormalAnnotation) rewrite.node();
    }

    private NodeRewrite rewrite;
}
