package poussecafe.doc.model.aggregatedoc;

import poussecafe.doc.StringNormalizer;
import poussecafe.domain.AggregateRoot;
import poussecafe.storable.IdentifiedStorableData;
import poussecafe.storable.Property;

import static poussecafe.check.Checks.checkThatValue;

public class AggregateDoc extends AggregateRoot<AggregateDocKey, AggregateDoc.Data> {

    void description(String description) {
        checkThatValue(description).notNull();
        getData().description().set(description);
    }

    public String description() {
        return getData().description().get();
    }

    public String boundedContextKey() {
        return getKey().boundedContextKey();
    }

    public String name() {
        return getKey().name();
    }

    public String id() {
        return StringNormalizer.normalizeString(name());
    }

    public static interface Data extends IdentifiedStorableData<AggregateDocKey> {

        Property<String> description();
    }
}
