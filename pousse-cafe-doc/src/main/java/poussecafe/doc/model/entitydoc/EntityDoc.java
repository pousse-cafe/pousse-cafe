package poussecafe.doc.model.entitydoc;

import poussecafe.doc.StringNormalizer;
import poussecafe.doc.model.BoundedContextComponentDoc;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.EntityData;
import poussecafe.property.Property;

import static poussecafe.check.Checks.checkThatValue;

public class EntityDoc extends AggregateRoot<EntityDocKey, EntityDoc.Data> {

    void componentDoc(BoundedContextComponentDoc boundedContextComponentDoc) {
        checkThatValue(boundedContextComponentDoc).notNull();
        getData().boundedContextComponentDoc().set(boundedContextComponentDoc);
    }

    public BoundedContextComponentDoc boundedContextComponentDoc() {
        return getData().boundedContextComponentDoc().get();
    }

    void keyClassName(String keyClassName) {
        getData().keyClassName().set(keyClassName);
    }

    public String keyClassName() {
        return getData().keyClassName().get();
    }

    public String id() {
        return StringNormalizer.normalizeString(boundedContextComponentDoc().componentDoc().name());
    }

    public static interface Data extends EntityData<EntityDocKey> {

        Property<BoundedContextComponentDoc> boundedContextComponentDoc();

        Property<String> keyClassName();
    }
}
