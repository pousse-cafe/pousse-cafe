package poussecafe.doc.model.servicedoc;

import poussecafe.doc.StringNormalizer;
import poussecafe.doc.model.BoundedContextComponentDoc;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.EntityData;
import poussecafe.property.Property;

import static poussecafe.check.Checks.checkThatValue;

public class ServiceDoc extends AggregateRoot<ServiceDocKey, ServiceDoc.Data> {

    void boundedContextComponentDoc(BoundedContextComponentDoc boundedContextComponentDoc) {
        checkThatValue(boundedContextComponentDoc).notNull();
        getData().boundedContextComponentDoc().set(boundedContextComponentDoc);
    }

    public BoundedContextComponentDoc boundedContextComponentDoc() {
        return getData().boundedContextComponentDoc().get();
    }

    public String id() {
        return StringNormalizer.normalizeString(boundedContextComponentDoc().componentDoc().name());
    }

    public static interface Data extends EntityData<ServiceDocKey> {

        Property<BoundedContextComponentDoc> boundedContextComponentDoc();
    }
}
