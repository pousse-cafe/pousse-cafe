package poussecafe.doc.model.entitydoc;

import poussecafe.doc.StringNormalizer;
import poussecafe.doc.model.aggregatedoc.AggregateDocKey;
import poussecafe.domain.AggregateRoot;
import poussecafe.storable.IdentifiedStorableData;
import poussecafe.storable.Property;

import static poussecafe.check.Checks.checkThatValue;

public class EntityDoc extends AggregateRoot<EntityDocKey, EntityDoc.Data> {

    void description(String description) {
        checkThatValue(description).notNull();
        getData().description().set(description);
    }

    public String description() {
        return getData().description().get();
    }

    public AggregateDocKey aggregateDocKey() {
        return getKey().aggregateDocKey();
    }

    public String name() {
        return getKey().name();
    }

    public String id() {
        return StringNormalizer.normalizeString(name());
    }

    public static interface Data extends IdentifiedStorableData<EntityDocKey> {

        Property<String> description();
    }
}
