package poussecafe.doc;

import com.sun.javadoc.ClassDoc;
import java.util.function.Consumer;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocFactory;
import poussecafe.doc.process.BoundedContextDocCreation;

import static poussecafe.check.Checks.checkThatValue;

public class BoundedContextDocCreator implements Consumer<ClassDoc> {

    public BoundedContextDocCreator(RootDocWrapper rootDocWrapper) {
        checkThatValue(rootDocWrapper).notNull();
        this.rootDocWrapper = rootDocWrapper;
    }

    private RootDocWrapper rootDocWrapper;

    @Override
    public void accept(ClassDoc classDoc) {
        if (!AnnotationsResolver.isIgnored(classDoc) && BoundedContextDocFactory.isBoundedContextDoc(classDoc)) {
            rootDocWrapper.debug("Adding bounded context with package " + classDoc.name());
            boundedContextDocCreation.addBoundedContextDoc(classDoc);
        }
    }

    private BoundedContextDocCreation boundedContextDocCreation;
}
