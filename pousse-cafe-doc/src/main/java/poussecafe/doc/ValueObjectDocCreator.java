package poussecafe.doc;

import javax.lang.model.element.TypeElement;
import jdk.javadoc.doclet.DocletEnvironment;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocId;
import poussecafe.doc.model.vodoc.ValueObjectDocFactory;
import poussecafe.doc.process.ValueObjectDocCreation;

public class ValueObjectDocCreator extends BoundedContextComponentDocCreator {

    public ValueObjectDocCreator(DocletEnvironment rootDocWrapper) {
        super(rootDocWrapper);
    }

    @Override
    protected boolean isComponentDoc(TypeElement classDoc) {
        return valueObjectDocFactory.isValueObjectDoc(classDoc);
    }

    private ValueObjectDocFactory valueObjectDocFactory;

    @Override
    protected String componentName() {
        return "value object";
    }

    @Override
    protected void addDoc(BoundedContextDocId boundedContextDocId,
            TypeElement componentClassDoc) {
        valueObjectDocCreation.addValueObjectDoc(boundedContextDocId, componentClassDoc);
    }

    private ValueObjectDocCreation valueObjectDocCreation;
}
