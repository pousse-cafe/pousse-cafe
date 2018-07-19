package poussecafe.doc;

import com.sun.javadoc.ClassDoc;
import java.util.function.Consumer;
import poussecafe.doc.model.servicedoc.ServiceDocFactory;
import poussecafe.doc.process.ServiceDocCreation;

import static poussecafe.check.Checks.checkThatValue;

public class ServiceDocCreator implements Consumer<ClassDoc> {

    public ServiceDocCreator(RootDocWrapper rootDocWrapper) {
        checkThatValue(rootDocWrapper).notNull();
        this.rootDocWrapper = rootDocWrapper;
    }

    private RootDocWrapper rootDocWrapper;

    @Override
    public void accept(ClassDoc classDoc) {
        if (ServiceDocFactory.isServiceDoc(classDoc)) {
            rootDocWrapper.debug("Adding service with class " + classDoc.name());
            serviceDocCreation.addServiceDoc(classDoc);
        }
    }

    private ServiceDocCreation serviceDocCreation;
}
