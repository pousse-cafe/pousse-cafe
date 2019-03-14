package poussecafe.doc.model.factorydoc;

import com.sun.javadoc.ClassDoc;
import poussecafe.doc.ClassDocPredicates;
import poussecafe.doc.model.BoundedContextComponentDoc;
import poussecafe.doc.model.ComponentDocFactory;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocKey;
import poussecafe.domain.DomainException;
import poussecafe.domain.Factory;

public class FactoryDocFactory extends Factory<FactoryDocKey, FactoryDoc, FactoryDoc.Attributes> {

    public FactoryDoc newFactoryDoc(BoundedContextDocKey boundedContextDocKey, ClassDoc classDoc) {
        if(!isFactoryDoc(classDoc)) {
            throw new DomainException("Class " + classDoc.name() + " is not a service");
        }

        String name = classDoc.simpleTypeName();
        FactoryDocKey key = FactoryDocKey.ofClassName(classDoc.qualifiedName());
        FactoryDoc factoryDoc = newAggregateWithKey(key);
        factoryDoc.attributes().boundedContextComponentDoc().value(new BoundedContextComponentDoc.Builder()
                .boundedContextDocKey(boundedContextDocKey)
                .componentDoc(componentDocFactory.buildDoc(name, classDoc))
                .build());

        return factoryDoc;
    }

    private ComponentDocFactory componentDocFactory;

    public static boolean isFactoryDoc(ClassDoc classDoc) {
        return ClassDocPredicates.documentsWithSuperclass(classDoc, Factory.class);
    }
}
