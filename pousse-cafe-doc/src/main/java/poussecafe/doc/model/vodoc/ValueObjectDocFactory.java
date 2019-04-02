package poussecafe.doc.model.vodoc;

import com.sun.javadoc.ClassDoc;
import poussecafe.doc.ClassDocPredicates;
import poussecafe.doc.model.BoundedContextComponentDoc;
import poussecafe.doc.model.ComponentDocFactory;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocId;
import poussecafe.domain.DomainException;
import poussecafe.domain.Factory;
import poussecafe.domain.ValueObject;

public class ValueObjectDocFactory extends Factory<ValueObjectDocId, ValueObjectDoc, ValueObjectDoc.Attributes> {

    public ValueObjectDoc newValueObjectDoc(BoundedContextDocId boundedContextDocId, ClassDoc doc) {
        if(!isValueObjectDoc(doc)) {
            throw new DomainException("Class " + doc.name() + " is not an entity");
        }

        String name = name(doc);
        ValueObjectDocId id = ValueObjectDocId.ofClassName(doc.qualifiedName());
        ValueObjectDoc valueObjectDoc = newAggregateWithId(id);
        valueObjectDoc.attributes().boundedContextComponentDoc().value(new BoundedContextComponentDoc.Builder()
                .boundedContextDocId(boundedContextDocId)
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
