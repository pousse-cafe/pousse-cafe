package poussecafe.doc.model.entitydoc;

import poussecafe.contextconfigurer.Aggregate;
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

    void keyClassName(String keyClassName) {
        data().keyClassName().set(keyClassName);
    }

    public String id() {
        return StringNormalizer.normalizeString(data().boundedContextComponentDoc().get().componentDoc().name());
    }

    public static interface Data extends EntityData<EntityDocKey> {

        Property<BoundedContextComponentDoc> boundedContextComponentDoc();

        Property<String> keyClassName();
    }
}
