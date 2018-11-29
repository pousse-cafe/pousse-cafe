package poussecafe.doc;

import com.sun.javadoc.PackageDoc;
import java.util.function.Consumer;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocFactory;
import poussecafe.doc.process.BoundedContextDocCreation;

import static poussecafe.check.Checks.checkThatValue;

public class BoundedContextDocCreator implements Consumer<PackageDoc> {

    public BoundedContextDocCreator(RootDocWrapper rootDocWrapper) {
        checkThatValue(rootDocWrapper).notNull();
        this.rootDocWrapper = rootDocWrapper;
    }

    private RootDocWrapper rootDocWrapper;

    @Override
    public void accept(PackageDoc classDoc) {
        if (BoundedContextDocFactory.isBoundedContextDoc(classDoc)) {
            Logger.debug("Adding bounded context from package " + classDoc.name());
            boundedContextDocCreation.addBoundedContextDoc(classDoc);
        }
    }

    private BoundedContextDocCreation boundedContextDocCreation;
}
