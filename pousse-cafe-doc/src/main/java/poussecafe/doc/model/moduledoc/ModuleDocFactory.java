package poussecafe.doc.model.moduledoc;

import javax.lang.model.element.PackageElement;
import poussecafe.doc.model.AnnotationsResolver;
import poussecafe.doc.model.ComponentDocFactory;
import poussecafe.domain.DomainException;
import poussecafe.domain.Factory;

public class ModuleDocFactory extends Factory<ModuleDocId, ModuleDoc, ModuleDoc.Attributes> {

    public ModuleDoc newModuleDoc(PackageElement packageDoc) {
        if(!isModuleDoc(packageDoc)) {
            throw new DomainException("Package " + packageDoc.getQualifiedName().toString() + " is not a valid module");
        }

        String name = annotationsResolver.module(packageDoc);
        ModuleDoc moduleDoc = newAggregateWithId(ModuleDocId.ofPackageName(packageDoc.getQualifiedName().toString()));
        moduleDoc.componentDoc(componentDocFactory.buildDoc(name, packageDoc));
        return moduleDoc;
    }

    private AnnotationsResolver annotationsResolver;

    private ComponentDocFactory componentDocFactory;

    public boolean isModuleDoc(PackageElement packageDoc) {
        return annotationsResolver.isModule(packageDoc);
    }

}
