package poussecafe.doc.model.factorydoc;

import com.sun.javadoc.ClassDoc;
import poussecafe.doc.ClassDocPredicates;
import poussecafe.doc.model.BoundedContextComponentDoc;
import poussecafe.doc.model.ComponentDocFactory;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocId;
import poussecafe.domain.DomainException;
import poussecafe.domain.Factory;

public class FactoryDocFactory extends Factory<FactoryDocId, FactoryDoc, FactoryDoc.Attributes> {

    public FactoryDoc newFactoryDoc(BoundedContextDocId boundedContextDocId, ClassDoc classDoc) {
        if(!isFactoryDoc(classDoc)) {
            throw new DomainException("Class " + classDoc.name() + " is not a service");
        }

        String name = classDoc.simpleTypeName();
        FactoryDocId id = FactoryDocId.ofClassName(classDoc.qualifiedName());
        FactoryDoc factoryDoc = newAggregateWithId(id);
        factoryDoc.attributes().boundedContextComponentDoc().value(new BoundedContextComponentDoc.Builder()
                .boundedContextDocId(boundedContextDocId)
                .componentDoc(componentDocFactory.buildDoc(name, classDoc))
                .build());

        return factoryDoc;
    }

    private ComponentDocFactory componentDocFactory;

    public static boolean isFactoryDoc(ClassDoc classDoc) {
        return ClassDocPredicates.documentsWithSuperclass(classDoc, Factory.class);
    }
}
