package poussecafe.doc.model.boundedcontextdoc;

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
        return getData().name().get();
    }

    void name(String name) {
        getData().name().set(name);
    }

    public String description() {
        return getData().description().get();
    }

    void description(String description) {
        getData().description().set(description);
    }

    public String id() {
        return name().toLowerCase().replaceAll("\\s+", "-");
    }

    public boolean hasDescription() {
        return getData().description() != null;
    }

    public static interface Data extends IdentifiedStorableData<String> {

        Property<String> name();

        Property<String> description();

        Property<String> packageName();
    }
}
