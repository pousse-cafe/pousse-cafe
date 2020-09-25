package poussecafe.doc.model.vodoc;

import javax.lang.model.element.TypeElement;
import poussecafe.doc.ClassDocPredicates;
import poussecafe.doc.model.ComponentDocFactory;
import poussecafe.doc.model.ModuleComponentDoc;
import poussecafe.doc.model.moduledoc.ModuleDocId;
import poussecafe.domain.DomainException;
import poussecafe.domain.Factory;
import poussecafe.domain.ValueObject;

public class ValueObjectDocFactory extends Factory<ValueObjectDocId, ValueObjectDoc, ValueObjectDoc.Attributes> {

    public ValueObjectDoc newValueObjectDoc(ModuleDocId moduleDocId, TypeElement doc) {
        if(!isValueObjectDoc(doc)) {
            throw new DomainException("Class " + doc.getQualifiedName() + " is not an entity");
        }

        String name = name(doc);
        ValueObjectDocId id = ValueObjectDocId.ofClassName(doc.getQualifiedName().toString());
        ValueObjectDoc valueObjectDoc = newAggregateWithId(id);
        valueObjectDoc.attributes().moduleComponentDoc().value(new ModuleComponentDoc.Builder()
                .moduleDocId(moduleDocId)
                .componentDoc(componentDocFactory.buildDoc(name, doc))
                .build());
        return valueObjectDoc;
    }

    private ComponentDocFactory componentDocFactory;

    public boolean isValueObjectDoc(TypeElement classDoc) {
        return classDocPredicates.documentsWithSuperinterface(classDoc, ValueObject.class) ||
                classDocPredicates.isEnum(classDoc);
    }

    private ClassDocPredicates classDocPredicates;

    public String name(TypeElement doc) {
        TypeElement classDoc = doc;
        return classDoc.getSimpleName().toString();
    }
}
