package poussecafe.doc;

import com.sun.javadoc.ClassDoc;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocKey;
import poussecafe.doc.model.servicedoc.ServiceDocFactory;
import poussecafe.doc.process.ServiceDocCreation;

public class ServiceDocCreator extends BoundedContextComponentDocCreator {

    public ServiceDocCreator(RootDocWrapper rootDocWrapper) {
        super(rootDocWrapper);
    }

    @Override
    protected boolean isComponentDoc(ClassDoc classDoc) {
        return ServiceDocFactory.isServiceDoc(classDoc);
    }

    @Override
    protected String componentName() {
        return "service";
    }

    @Override
    protected void addDoc(BoundedContextDocKey boundedContextDocKey,
            ClassDoc componentClassDoc) {
        serviceDocCreation.addServiceDoc(boundedContextDocKey, componentClassDoc);
    }

    private ServiceDocCreation serviceDocCreation;
}
