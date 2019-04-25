package poussecafe.doc;

import javax.lang.model.element.TypeElement;
import jdk.javadoc.doclet.DocletEnvironment;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocId;
import poussecafe.doc.model.servicedoc.ServiceDocFactory;
import poussecafe.doc.process.ServiceDocCreation;

public class ServiceDocCreator extends BoundedContextComponentDocCreator {

    public ServiceDocCreator(DocletEnvironment environment) {
        super(environment);
    }

    @Override
    protected boolean isComponentDoc(TypeElement classDoc) {
        return serviceDocFactory.isServiceDoc(classDoc);
    }

    private ServiceDocFactory serviceDocFactory;

    @Override
    protected String componentName() {
        return "service";
    }

    @Override
    protected void addDoc(BoundedContextDocId boundedContextDocId,
            TypeElement componentClassDoc) {
        serviceDocCreation.addServiceDoc(boundedContextDocId, componentClassDoc);
    }

    private ServiceDocCreation serviceDocCreation;
}
