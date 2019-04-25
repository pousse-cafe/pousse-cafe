package poussecafe.doc;

import javax.lang.model.element.TypeElement;
import jdk.javadoc.doclet.DocletEnvironment;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocId;
import poussecafe.doc.model.factorydoc.FactoryDocFactory;
import poussecafe.doc.process.FactoryDocCreation;

public class FactoryDocCreator extends BoundedContextComponentDocCreator {

    public FactoryDocCreator(DocletEnvironment rootDocWrapper) {
        super(rootDocWrapper);
    }

    @Override
    protected boolean isComponentDoc(TypeElement classDoc) {
        return factoryDocFactory.isFactoryDoc(classDoc);
    }

    private FactoryDocFactory factoryDocFactory;

    @Override
    protected String componentName() {
        return "factory";
    }

    @Override
    protected void addDoc(BoundedContextDocId boundedContextDocId,
            TypeElement componentClassDoc) {
        factoryDocCreation.addFactoryDoc(boundedContextDocId, componentClassDoc);
    }

    private FactoryDocCreation factoryDocCreation;
}
