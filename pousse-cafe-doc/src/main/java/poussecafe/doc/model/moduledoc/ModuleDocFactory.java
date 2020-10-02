package poussecafe.doc.model.moduledoc;

import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import poussecafe.doc.ClassDocPredicates;
import poussecafe.doc.model.AnnotationsResolver;
import poussecafe.doc.model.ComponentDocFactory;
import poussecafe.domain.AggregateFactory;
import poussecafe.domain.DomainException;
import poussecafe.domain.Module;

public class ModuleDocFactory extends AggregateFactory<ModuleDocId, ModuleDoc, ModuleDoc.Attributes> {

    @Deprecated(since = "0.17")
    public ModuleDoc newModuleDoc(PackageElement packageDoc) {
        if(!isModuleDoc(packageDoc)) {
            throw new DomainException("Package " + packageDoc.getQualifiedName().toString() + " is not a valid module");
        }

        String name = annotationsResolver.module(packageDoc);
        ModuleDoc moduleDoc = newAggregateWithId(moduleDocId(packageDoc));
        moduleDoc.componentDoc(componentDocFactory.buildDoc(name, packageDoc));
        return moduleDoc;
    }

    private AnnotationsResolver annotationsResolver;

    public ModuleDocId moduleDocId(PackageElement packageDoc) {
        return ModuleDocId.ofPackageName(packageDoc.getQualifiedName().toString());
    }

    private ComponentDocFactory componentDocFactory;

    @Deprecated(since = "0.17")
    public boolean isModuleDoc(PackageElement packageDoc) {
        return annotationsResolver.isModule(packageDoc);
    }

    public boolean isModuleDoc(TypeElement doc) {
        return classDocPredicates.documentsWithSuperinterface(doc, Module.class);
    }

    private ClassDocPredicates classDocPredicates;

    public ModuleDoc newModuleDoc(TypeElement doc) {
        if(!isModuleDoc(doc)) {
            throw new DomainException("Class " + doc.getQualifiedName().toString() + " is not a valid module");
        }

        String name = name(doc);
        PackageElement packageElement = (PackageElement) doc.getEnclosingElement();
        ModuleDoc moduleDoc = newAggregateWithId(moduleDocId(packageElement));
        moduleDoc.componentDoc(componentDocFactory.buildDoc(name, doc));
        return moduleDoc;
    }

    public String name(TypeElement doc) {
        return doc.getSimpleName().toString();
    }
}
