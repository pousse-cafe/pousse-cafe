package poussecafe.doc;

import com.sun.javadoc.ClassDoc;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocId;
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
    protected void addDoc(BoundedContextDocId boundedContextDocId,
            ClassDoc componentClassDoc) {
        serviceDocCreation.addServiceDoc(boundedContextDocId, componentClassDoc);
    }

    private ServiceDocCreation serviceDocCreation;
}
