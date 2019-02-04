package poussecafe.doc;

import com.sun.javadoc.ClassDoc;
import java.util.Objects;
import java.util.function.Consumer;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDoc;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocKey;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocRepository;

public abstract class BoundedContextComponentDocCreator implements Consumer<ClassDoc> {

    public BoundedContextComponentDocCreator(RootDocWrapper rootDocWrapper) {
        Objects.requireNonNull(rootDocWrapper);
        this.rootDocWrapper = rootDocWrapper;
    }

    private RootDocWrapper rootDocWrapper;

    @Override
    public void accept(ClassDoc classDoc) {
        if (isComponentDoc(classDoc)) {
            BoundedContextDoc boundedContextDoc = boundedContextDocRepository
                    .findByPackageNamePrefixing(classDoc.qualifiedName());
            if (boundedContextDoc != null) {
                Logger.debug("Adding " + componentName() + " with class " + classDoc.qualifiedTypeName());
                addDoc(boundedContextDoc.data().key().get(), classDoc);
            }
        }
    }

    protected abstract boolean isComponentDoc(ClassDoc classDoc);

    protected abstract String componentName();

    private BoundedContextDocRepository boundedContextDocRepository;

    protected abstract void addDoc(BoundedContextDocKey boundedContextDocKey,
            ClassDoc componentClassDoc);

    protected RootDocWrapper rootDocWrapper() {
        return rootDocWrapper;
    }
}
