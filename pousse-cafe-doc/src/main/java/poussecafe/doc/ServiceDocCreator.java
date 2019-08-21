package poussecafe.doc;

import javax.lang.model.element.TypeElement;
import jdk.javadoc.doclet.DocletEnvironment;
import poussecafe.doc.model.moduledoc.ModuleDocId;
import poussecafe.doc.model.servicedoc.ServiceDocFactory;
import poussecafe.doc.process.ServiceDocCreation;

public class ServiceDocCreator extends ModuleComponentDocCreator {

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
    protected void addDoc(ModuleDocId moduleDocId,
            TypeElement componentClassDoc) {
        serviceDocCreation.addServiceDoc(moduleDocId, componentClassDoc);
    }

    private ServiceDocCreation serviceDocCreation;
}
