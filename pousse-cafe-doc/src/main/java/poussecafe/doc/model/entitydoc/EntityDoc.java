package poussecafe.doc.model.entitydoc;

import java.util.Objects;
import poussecafe.context.Aggregate;
import poussecafe.doc.StringNormalizer;
import poussecafe.doc.model.BoundedContextComponentDoc;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.EntityData;
import poussecafe.property.Property;

@Aggregate(
  factory = EntityDocFactory.class,
  repository = EntityDocRepository.class
)
public class EntityDoc extends AggregateRoot<EntityDocKey, EntityDoc.Data> {

    void componentDoc(BoundedContextComponentDoc boundedContextComponentDoc) {
        Objects.requireNonNull(boundedContextComponentDoc);
        data().boundedContextComponentDoc().set(boundedContextComponentDoc);
    }

    public BoundedContextComponentDoc boundedContextComponentDoc() {
        return data().boundedContextComponentDoc().get();
    }

    void keyClassName(String keyClassName) {
        data().keyClassName().set(keyClassName);
    }

    public String keyClassName() {
        return data().keyClassName().get();
    }

    public String id() {
        return StringNormalizer.normalizeString(boundedContextComponentDoc().componentDoc().name());
    }

    public static interface Data extends EntityData<EntityDocKey> {

        Property<BoundedContextComponentDoc> boundedContextComponentDoc();

        Property<String> keyClassName();
    }
}
