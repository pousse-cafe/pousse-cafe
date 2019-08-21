package poussecafe.doc;

import java.util.function.Consumer;
import javax.lang.model.element.PackageElement;
import poussecafe.doc.model.moduledoc.ModuleDocFactory;
import poussecafe.doc.process.ModuleDocCreation;

public class ModuleDocCreator implements Consumer<PackageElement> {

    @Override
    public void accept(PackageElement classDoc) {
        if (moduleDocFactory.isModuleDoc(classDoc)) {
            Logger.debug("Adding module from package " + classDoc.getQualifiedName().toString());
            moduleDocCreation.addModuleDoc(classDoc);
        }
    }

    private ModuleDocFactory moduleDocFactory;

    private ModuleDocCreation moduleDocCreation;
}
