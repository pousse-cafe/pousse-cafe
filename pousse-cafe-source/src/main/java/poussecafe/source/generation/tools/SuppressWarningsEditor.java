package poussecafe.source.generation.tools;

import org.eclipse.jdt.core.dom.ArrayInitializer;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.StringLiteral;

import static java.util.Objects.requireNonNull;

@SuppressWarnings("unchecked")
public class SuppressWarningsEditor {

    public void addWarning(String warning) {
        Expression currentValue = annotationEditor.getValue();
        if(currentValue == null
                || currentValue instanceof SimpleName) {
            annotationEditor.setValue(newStringLiteral(warning));
        } else if(currentValue instanceof StringLiteral) {
            if(!isWarning(currentValue, warning)) {
                var array = annotationEditor.rewrite().ast().newArrayInitializer();
                array.expressions().add(newStringLiteral(warning));
                annotationEditor.setValue(array);
            }
        } else if(currentValue instanceof ArrayInitializer) {
            var currentArray = (ArrayInitializer) currentValue;
            for(Object expression : currentArray.expressions()) {
                if(isWarning(expression, warning)) {
                    return; // Warning is already present
                }
            }
            var arrayEditor = new NodeRewrite(annotationEditor.rewrite().rewrite(), currentArray);
            arrayEditor.listRewrite(ArrayInitializer.EXPRESSIONS_PROPERTY).insertLast(newStringLiteral(warning), null);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    private boolean isWarning(Object expression, String warning) {
        StringLiteral literal = (StringLiteral) expression;
        return literal.getLiteralValue().equals(warning);
    }

    private StringLiteral newStringLiteral(String value) {
        var literal = annotationEditor.rewrite().ast().newStringLiteral();
        literal.setLiteralValue(value);
        return literal;
    }

    public SuppressWarningsEditor(SingleMemberAnnotationEditor annotationEditor) {
        requireNonNull(annotationEditor);
        this.annotationEditor = annotationEditor;
    }

    private SingleMemberAnnotationEditor annotationEditor;
}
