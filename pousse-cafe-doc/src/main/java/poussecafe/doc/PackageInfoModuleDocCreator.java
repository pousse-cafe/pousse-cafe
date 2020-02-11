package poussecafe.doc;

import java.util.function.Consumer;
import javax.lang.model.element.PackageElement;
import poussecafe.doc.model.moduledoc.ModuleDocFactory;
import poussecafe.doc.model.moduledoc.ModuleDocId;
import poussecafe.doc.model.moduledoc.ModuleDocRepository;
import poussecafe.doc.process.ModuleDocCreation;

@Deprecated(since = "0.17")
public class PackageInfoModuleDocCreator implements Consumer<PackageElement> {

    @Override
    public void accept(PackageElement classDoc) {
        if (moduleDocFactory.isModuleDoc(classDoc)) {
            Logger.warn("package-info based module definition is deprecated, use a module class instead for " + classDoc.getQualifiedName().toString());
            if(!moduleAlreadyDefined(classDoc)) {
                Logger.debug("Adding module from package " + classDoc.getQualifiedName().toString());
                moduleDocCreation.addModuleDoc(classDoc);
            }
        }
    }

    private boolean moduleAlreadyDefined(PackageElement classDoc) {
        ModuleDocId moduleId = moduleDocFactory.moduleDocId(classDoc);
        return moduleDocRepository.existsById(moduleId);
    }

    private ModuleDocFactory moduleDocFactory;

    private ModuleDocRepository moduleDocRepository;

    private ModuleDocCreation moduleDocCreation;
}
