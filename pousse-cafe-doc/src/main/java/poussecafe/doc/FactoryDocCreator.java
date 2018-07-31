package poussecafe.doc;

import com.sun.javadoc.ClassDoc;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocKey;
import poussecafe.doc.model.factorydoc.FactoryDocFactory;
import poussecafe.doc.process.FactoryDocCreation;

public class FactoryDocCreator extends BoundedContextComponentDocCreator {

    public FactoryDocCreator(RootDocWrapper rootDocWrapper) {
        super(rootDocWrapper);
    }

    @Override
    protected boolean isComponentDoc(ClassDoc classDoc) {
        return FactoryDocFactory.isFactoryDoc(classDoc);
    }

    @Override
    protected String componentName() {
        return "factory";
    }

    @Override
    protected void addDoc(BoundedContextDocKey boundedContextDocKey,
            ClassDoc componentClassDoc) {
        factoryDocCreation.addFactoryDoc(boundedContextDocKey, componentClassDoc);
    }

    private FactoryDocCreation factoryDocCreation;
}
