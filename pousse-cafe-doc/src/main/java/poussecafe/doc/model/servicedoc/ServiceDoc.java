package poussecafe.doc.model.servicedoc;

import poussecafe.doc.StringNormalizer;
import poussecafe.domain.AggregateRoot;
import poussecafe.storable.IdentifiedStorableData;
import poussecafe.storable.Property;

import static poussecafe.check.Checks.checkThatValue;

public class ServiceDoc extends AggregateRoot<ServiceDocKey, ServiceDoc.Data> {

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

    public static interface Data extends IdentifiedStorableData<ServiceDocKey> {

        Property<String> description();
    }
}
