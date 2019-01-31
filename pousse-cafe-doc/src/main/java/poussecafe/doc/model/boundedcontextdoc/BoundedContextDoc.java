package poussecafe.doc.model.boundedcontextdoc;

import poussecafe.contextconfigurer.Aggregate;
import poussecafe.doc.StringNormalizer;
import poussecafe.doc.model.ComponentDoc;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.EntityData;
import poussecafe.property.Property;

@Aggregate(
    factory = BoundedContextDocFactory.class,
    repository = BoundedContextDocRepository.class
)
public class BoundedContextDoc extends AggregateRoot<BoundedContextDocKey, BoundedContextDoc.Data> {

    void componentDoc(ComponentDoc componentDoc) {
        data().componentDoc().set(componentDoc);
    }

    public ComponentDoc componentDoc() {
        return data().componentDoc().get();
    }

    public String packageName() {
        return getKey().getValue();
    }

    public String id() {
        return StringNormalizer.normalizeString(componentDoc().name());
    }

    public static interface Data extends EntityData<BoundedContextDocKey> {

        Property<ComponentDoc> componentDoc();
    }
}
