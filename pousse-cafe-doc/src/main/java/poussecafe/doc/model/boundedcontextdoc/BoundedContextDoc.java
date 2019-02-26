package poussecafe.doc.model.boundedcontextdoc;

import poussecafe.attribute.Attribute;
import poussecafe.discovery.Aggregate;
import poussecafe.doc.StringNormalizer;
import poussecafe.doc.model.ComponentDoc;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.EntityAttributes;

@Aggregate(
    factory = BoundedContextDocFactory.class,
    repository = BoundedContextDocRepository.class
)
public class BoundedContextDoc extends AggregateRoot<BoundedContextDocKey, BoundedContextDoc.Attributes> {

    void componentDoc(ComponentDoc componentDoc) {
        attributes().componentDoc().value(componentDoc);
    }

    public String packageName() {
        return attributes().key().value().getValue();
    }

    public String id() {
        return StringNormalizer.normalizeString(attributes().componentDoc().value().name());
    }

    public static interface Attributes extends EntityAttributes<BoundedContextDocKey> {

        Attribute<ComponentDoc> componentDoc();
    }
}
