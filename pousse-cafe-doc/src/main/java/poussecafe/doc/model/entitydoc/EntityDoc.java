package poussecafe.doc.model.entitydoc;

import poussecafe.attribute.Attribute;
import poussecafe.discovery.Aggregate;
import poussecafe.doc.StringNormalizer;
import poussecafe.doc.model.ModuleComponentDoc;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.EntityAttributes;

@Aggregate(
  factory = EntityDocFactory.class,
  repository = EntityDocRepository.class
)
public class EntityDoc extends AggregateRoot<EntityDocId, EntityDoc.Attributes> {

    void idClassName(String idClassName) {
        attributes().idClassName().value(idClassName);
    }

    public String id() {
        return StringNormalizer.normalizeString(attributes().moduleComponentDoc().value().componentDoc().name());
    }

    public static interface Attributes extends EntityAttributes<EntityDocId> {

        Attribute<ModuleComponentDoc> moduleComponentDoc();

        Attribute<String> idClassName();
    }
}
