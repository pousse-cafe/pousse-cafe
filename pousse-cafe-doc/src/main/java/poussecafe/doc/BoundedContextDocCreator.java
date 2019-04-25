package poussecafe.doc;

import java.util.function.Consumer;
import javax.lang.model.element.PackageElement;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocFactory;
import poussecafe.doc.process.BoundedContextDocCreation;

public class BoundedContextDocCreator implements Consumer<PackageElement> {

    @Override
    public void accept(PackageElement classDoc) {
        if (boundedContextDocFactory.isBoundedContextDoc(classDoc)) {
            Logger.debug("Adding bounded context from package " + classDoc.getQualifiedName().toString());
            boundedContextDocCreation.addBoundedContextDoc(classDoc);
        }
    }

    private BoundedContextDocFactory boundedContextDocFactory;

    private BoundedContextDocCreation boundedContextDocCreation;
}
