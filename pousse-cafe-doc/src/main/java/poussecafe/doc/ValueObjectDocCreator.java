package poussecafe.doc;

import com.sun.javadoc.ClassDoc;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocKey;
import poussecafe.doc.model.vodoc.ValueObjectDocFactory;
import poussecafe.doc.process.ValueObjectDocCreation;

public class ValueObjectDocCreator extends BoundedContextComponentDocCreator {

    public ValueObjectDocCreator(RootDocWrapper rootDocWrapper) {
        super(rootDocWrapper);
    }

    @Override
    protected boolean isComponentDoc(ClassDoc classDoc) {
        return ValueObjectDocFactory.isValueObjectDoc(classDoc);
    }

    @Override
    protected String componentName() {
        return "value object";
    }

    @Override
    protected void addDoc(BoundedContextDocKey boundedContextDocKey,
            ClassDoc componentClassDoc) {
        valueObjectDocCreation.addValueObjectDoc(boundedContextDocKey, componentClassDoc);
    }

    private ValueObjectDocCreation valueObjectDocCreation;
}
