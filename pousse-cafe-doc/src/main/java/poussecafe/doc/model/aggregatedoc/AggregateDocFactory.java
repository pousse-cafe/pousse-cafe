package poussecafe.doc.model.aggregatedoc;

import com.sun.javadoc.ClassDoc;
import poussecafe.doc.ClassDocPredicates;
import poussecafe.doc.model.BoundedContextComponentDoc;
import poussecafe.doc.model.ComponentDocFactory;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocKey;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.DomainException;
import poussecafe.domain.Factory;

public class AggregateDocFactory extends Factory<AggregateDocKey, AggregateDoc, AggregateDoc.Attributes> {

    public AggregateDoc newAggregateDoc(BoundedContextDocKey boundedContextDocKey, ClassDoc aggregateClassDoc) {
        if(!isAggregateDoc(aggregateClassDoc)) {
            throw new DomainException("Class " + aggregateClassDoc.name() + " is not an aggregate root");
        }

        AggregateDocKey key = AggregateDocKey.ofClassName(aggregateClassDoc.qualifiedTypeName());
        AggregateDoc aggregateDoc = newAggregateWithKey(key);

        String name = name(aggregateClassDoc);
        aggregateDoc.attributes().boundedContextComponentDoc().value(new BoundedContextComponentDoc.Builder()
                .boundedContextDocKey(boundedContextDocKey)
                .componentDoc(componentDocFactory.buildDoc(name, aggregateClassDoc))
                .build());

        aggregateDoc.attributes().keyClassName().value(keyClassName(aggregateClassDoc));

        return aggregateDoc;
    }

    private ComponentDocFactory componentDocFactory;

    private String keyClassName(ClassDoc aggregateClassDoc) {
        return aggregateClassDoc.superclassType().asParameterizedType().typeArguments()[KEY_TYPE_INDEX].qualifiedTypeName();
    }

    private static final int KEY_TYPE_INDEX = 0;

    public static String name(ClassDoc classDoc) {
        return classDoc.simpleTypeName();
    }

    public static boolean isAggregateDoc(ClassDoc classDoc) {
        return ClassDocPredicates.documentsWithSuperclass(classDoc, AggregateRoot.class);
    }
}
