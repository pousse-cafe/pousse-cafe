package poussecafe.doc.model.factorydoc;

import javax.lang.model.element.TypeElement;
import poussecafe.doc.ClassDocPredicates;
import poussecafe.doc.model.ModuleComponentDoc;
import poussecafe.doc.model.ComponentDocFactory;
import poussecafe.doc.model.moduledoc.ModuleDocId;
import poussecafe.domain.DomainException;
import poussecafe.domain.Factory;

public class FactoryDocFactory extends Factory<FactoryDocId, FactoryDoc, FactoryDoc.Attributes> {

    public FactoryDoc newFactoryDoc(ModuleDocId moduleDocId, TypeElement classDoc) {
        if(!isFactoryDoc(classDoc)) {
            throw new DomainException("Class " + classDoc.getQualifiedName() + " is not a factory");
        }

        String name = classDoc.getSimpleName().toString();
        FactoryDocId id = FactoryDocId.ofClassName(classDoc.getQualifiedName().toString());
        FactoryDoc factoryDoc = newAggregateWithId(id);
        factoryDoc.attributes().moduleComponentDoc().value(new ModuleComponentDoc.Builder()
                .moduleDocId(moduleDocId)
                .componentDoc(componentDocFactory.buildDoc(name, classDoc))
                .build());

        return factoryDoc;
    }

    private ComponentDocFactory componentDocFactory;

    public boolean isFactoryDoc(TypeElement classDoc) {
        return classDocPredicates.documentsWithSuperclass(classDoc, Factory.class);
    }

    private ClassDocPredicates classDocPredicates;
}
