package poussecafe.doc;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import javax.lang.model.element.TypeElement;
import jdk.javadoc.doclet.DocletEnvironment;
import poussecafe.doc.model.moduledoc.ModuleDoc;
import poussecafe.doc.model.moduledoc.ModuleDocRepository;
import poussecafe.doc.model.moduledoc.ModuleDocId;

public abstract class ModuleComponentDocCreator implements Consumer<TypeElement> {

    public ModuleComponentDocCreator(DocletEnvironment rootDocWrapper) {
        Objects.requireNonNull(rootDocWrapper);
        this.rootDocWrapper = rootDocWrapper;
    }

    private DocletEnvironment rootDocWrapper;

    @Override
    public void accept(TypeElement classDoc) {
        if (isComponentDoc(classDoc)) {
            Optional<ModuleDoc> moduleDoc = moduleDocRepository
                    .findByPackageNamePrefixing(classDoc.getQualifiedName().toString());
            if (moduleDoc.isPresent()) {
                ModuleDocId moduleId = moduleDoc.get().attributes().identifier().value();
                Logger.debug("Adding " + componentName() + " with class " + classDoc.getQualifiedName().toString() + " to BC " + moduleId);
                addDoc(moduleId, classDoc);
            } else {
                Logger.warn("Could not add component with missing bounded context: " + classDoc.getQualifiedName().toString());
            }
        }
    }

    protected abstract boolean isComponentDoc(TypeElement classDoc);

    protected abstract String componentName();

    private ModuleDocRepository moduleDocRepository;

    protected abstract void addDoc(ModuleDocId moduleDocId,
            TypeElement componentClassDoc);

    protected DocletEnvironment rootDocWrapper() {
        return rootDocWrapper;
    }
}
