package poussecafe.doc;

import java.util.Objects;
import java.util.function.Consumer;
import javax.lang.model.element.TypeElement;
import jdk.javadoc.doclet.DocletEnvironment;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDoc;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocId;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocRepository;

public abstract class BoundedContextComponentDocCreator implements Consumer<TypeElement> {

    public BoundedContextComponentDocCreator(DocletEnvironment rootDocWrapper) {
        Objects.requireNonNull(rootDocWrapper);
        this.rootDocWrapper = rootDocWrapper;
    }

    private DocletEnvironment rootDocWrapper;

    @Override
    public void accept(TypeElement classDoc) {
        if (isComponentDoc(classDoc)) {
            BoundedContextDoc boundedContextDoc = boundedContextDocRepository
                    .findByPackageNamePrefixing(classDoc.getQualifiedName().toString());
            if (boundedContextDoc != null) {
                BoundedContextDocId boundedContextId = boundedContextDoc.attributes().identifier().value();
                Logger.debug("Adding " + componentName() + " with class " + classDoc.getQualifiedName().toString() + " to BC " + boundedContextId);
                addDoc(boundedContextId, classDoc);
            } else {
                Logger.warn("Could not add component with missing bounded context: " + classDoc.getQualifiedName().toString());
            }
        }
    }

    protected abstract boolean isComponentDoc(TypeElement classDoc);

    protected abstract String componentName();

    private BoundedContextDocRepository boundedContextDocRepository;

    protected abstract void addDoc(BoundedContextDocId boundedContextDocId,
            TypeElement componentClassDoc);

    protected DocletEnvironment rootDocWrapper() {
        return rootDocWrapper;
    }
}
