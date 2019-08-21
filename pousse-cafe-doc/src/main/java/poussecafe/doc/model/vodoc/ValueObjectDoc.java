package poussecafe.doc.model.vodoc;

import poussecafe.attribute.Attribute;
import poussecafe.discovery.Aggregate;
import poussecafe.doc.StringNormalizer;
import poussecafe.doc.model.ModuleComponentDoc;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.EntityAttributes;

/**
 * <p>ValueObjectDoc describes the documentation of a Value Object in a given Bounded Context.</p>
 */
@Aggregate(
  factory = ValueObjectDocFactory.class,
  repository = ValueObjectDocRepository.class
)
public class ValueObjectDoc extends AggregateRoot<ValueObjectDocId, ValueObjectDoc.Attributes> {

    public String id() {
        return StringNormalizer.normalizeString(attributes().moduleComponentDoc().value().componentDoc().name());
    }

    public static interface Attributes extends EntityAttributes<ValueObjectDocId> {

        Attribute<ModuleComponentDoc> moduleComponentDoc();
    }
}
