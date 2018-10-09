package poussecafe.doc.model.boundedcontextdoc;

import poussecafe.doc.StringNormalizer;
import poussecafe.doc.model.ComponentDoc;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.EntityData;
import poussecafe.property.Property;

import static poussecafe.check.Checks.checkThatValue;

public class BoundedContextDoc extends AggregateRoot<BoundedContextDocKey, BoundedContextDoc.Data> {

    void componentDoc(ComponentDoc componentDoc) {
        checkThatValue(componentDoc).notNull();
        getData().componentDoc().set(componentDoc);
    }

    public ComponentDoc componentDoc() {
        return getData().componentDoc().get();
    }

    void packageName(String packageName) {
        checkThatValue(packageName).notNull();
        getData().packageName().set(packageName);
    }

    public String packageName() {
        return getData().packageName().get();
    }

    public String id() {
        return StringNormalizer.normalizeString(componentDoc().name());
    }

    public static interface Data extends EntityData<BoundedContextDocKey> {

        Property<ComponentDoc> componentDoc();

        Property<String> packageName();
    }
}
