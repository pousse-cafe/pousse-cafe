package poussecafe.doc.process;

import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import poussecafe.doc.model.moduledoc.ModuleDoc;
import poussecafe.doc.model.moduledoc.ModuleDocFactory;
import poussecafe.doc.model.moduledoc.ModuleDocRepository;
import poussecafe.process.DomainProcess;

public class ModuleDocCreation extends DomainProcess {

    public void addModuleDoc(PackageElement classDoc) {
        ModuleDoc moduleDoc = moduleDocFactory.newModuleDoc(classDoc);
        runInTransaction(ModuleDoc.class, () -> moduleDocRepository.add(moduleDoc));
    }

    private ModuleDocFactory moduleDocFactory;

    private ModuleDocRepository moduleDocRepository;

    public void addModuleDoc(TypeElement classDoc) {
        ModuleDoc moduleDoc = moduleDocFactory.newModuleDoc(classDoc);
        runInTransaction(ModuleDoc.class, () -> moduleDocRepository.add(moduleDoc));
    }
}
