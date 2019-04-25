package poussecafe.doc.model.factorydoc;

import javax.lang.model.element.TypeElement;
import poussecafe.doc.ClassDocPredicates;
import poussecafe.doc.model.BoundedContextComponentDoc;
import poussecafe.doc.model.ComponentDocFactory;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocId;
import poussecafe.domain.DomainException;
import poussecafe.domain.Factory;

public class FactoryDocFactory extends Factory<FactoryDocId, FactoryDoc, FactoryDoc.Attributes> {

    public FactoryDoc newFactoryDoc(BoundedContextDocId boundedContextDocId, TypeElement classDoc) {
        if(!isFactoryDoc(classDoc)) {
            throw new DomainException("Class " + classDoc.getQualifiedName() + " is not a service");
        }

        String name = classDoc.getSimpleName().toString();
        FactoryDocId id = FactoryDocId.ofClassName(classDoc.getQualifiedName().toString());
        FactoryDoc factoryDoc = newAggregateWithId(id);
        factoryDoc.attributes().boundedContextComponentDoc().value(new BoundedContextComponentDoc.Builder()
                .boundedContextDocId(boundedContextDocId)
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
