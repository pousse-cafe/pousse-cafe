package poussecafe.doc.model.entitydoc;

import poussecafe.attribute.Attribute;
import poussecafe.contextconfigurer.Aggregate;
import poussecafe.doc.StringNormalizer;
import poussecafe.doc.model.BoundedContextComponentDoc;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.EntityAttributes;

@Aggregate(
  factory = EntityDocFactory.class,
  repository = EntityDocRepository.class
)
public class EntityDoc extends AggregateRoot<EntityDocKey, EntityDoc.Attributes> {

    void keyClassName(String keyClassName) {
        attributes().keyClassName().value(keyClassName);
    }

    public String id() {
        return StringNormalizer.normalizeString(attributes().boundedContextComponentDoc().value().componentDoc().name());
    }

    public static interface Attributes extends EntityAttributes<EntityDocKey> {

        Attribute<BoundedContextComponentDoc> boundedContextComponentDoc();

        Attribute<String> keyClassName();
    }
}
