package poussecafe.doc.model.vodoc;

import poussecafe.doc.StringNormalizer;
import poussecafe.doc.model.BoundedContextComponentDoc;
import poussecafe.domain.AggregateRoot;
import poussecafe.storable.IdentifiedStorableData;
import poussecafe.storable.Property;

import static poussecafe.check.Checks.checkThatValue;

public class ValueObjectDoc extends AggregateRoot<ValueObjectDocKey, ValueObjectDoc.Data> {

    void boundedContextComponentDoc(BoundedContextComponentDoc componentDoc) {
        checkThatValue(componentDoc).notNull();
        getData().boundedContextComponentDoc().set(componentDoc);
    }

    public BoundedContextComponentDoc boundedContextComponentDoc() {
        return getData().boundedContextComponentDoc().get();
    }

    public String id() {
        return StringNormalizer.normalizeString(boundedContextComponentDoc().componentDoc().name());
    }

    public static interface Data extends IdentifiedStorableData<ValueObjectDocKey> {

        Property<BoundedContextComponentDoc> boundedContextComponentDoc();
    }
}
