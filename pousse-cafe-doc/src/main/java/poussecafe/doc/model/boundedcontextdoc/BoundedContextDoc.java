package poussecafe.doc.model.boundedcontextdoc;

import poussecafe.doc.StringNormalizer;
import poussecafe.domain.AggregateRoot;
import poussecafe.storable.IdentifiedStorableData;
import poussecafe.storable.Property;

import static poussecafe.check.Checks.checkThatValue;

public class BoundedContextDoc extends AggregateRoot<String, BoundedContextDoc.Data> {

    void packageName(String packageName) {
        checkThatValue(packageName).notNull();
        getData().packageName().set(packageName);
    }

    public String packageName() {
        return getData().packageName().get();
    }

    public String name() {
        return getKey();
    }

    public String description() {
        return getData().description().get();
    }

    void description(String description) {
        getData().description().set(description);
    }

    public String id() {
        return StringNormalizer.normalizeString(name());
    }

    public boolean hasDescription() {
        return getData().description() != null;
    }

    public static interface Data extends IdentifiedStorableData<String> {

        Property<String> description();

        Property<String> packageName();
    }
}
