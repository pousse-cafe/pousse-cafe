package poussecafe.doc.model.vodoc;

import com.sun.javadoc.ClassDoc;
import poussecafe.doc.ClassDocPredicates;
import poussecafe.doc.model.BoundedContextComponentDoc;
import poussecafe.doc.model.ComponentDocFactory;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocKey;
import poussecafe.domain.DomainException;
import poussecafe.domain.Factory;
import poussecafe.domain.ValueObject;

public class ValueObjectDocFactory extends Factory<ValueObjectDocKey, ValueObjectDoc, ValueObjectDoc.Attributes> {

    public ValueObjectDoc newValueObjectDoc(BoundedContextDocKey boundedContextDocKey, ClassDoc doc) {
        if(!isValueObjectDoc(doc)) {
            throw new DomainException("Class " + doc.name() + " is not an entity");
        }

        String name = name(doc);
        ValueObjectDocKey key = ValueObjectDocKey.ofClassName(doc.qualifiedName());
        ValueObjectDoc valueObjectDoc = newAggregateWithKey(key);
        valueObjectDoc.attributes().boundedContextComponentDoc().value(new BoundedContextComponentDoc.Builder()
                .boundedContextDocKey(boundedContextDocKey)
                .componentDoc(componentDocFactory.buildDoc(name, doc))
                .build());
        return valueObjectDoc;
    }

    private ComponentDocFactory componentDocFactory;

    public static boolean isValueObjectDoc(ClassDoc classDoc) {
        return ClassDocPredicates.documentsWithSuperinterface(classDoc, ValueObject.class) || classDoc.isEnum();
    }

    public static String name(ClassDoc doc) {
        ClassDoc classDoc = doc;
        return classDoc.simpleTypeName();
    }
}
