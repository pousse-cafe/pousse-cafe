package poussecafe.doc.model.servicedoc;

import java.util.Objects;
import poussecafe.context.Aggregate;
import poussecafe.doc.StringNormalizer;
import poussecafe.doc.model.BoundedContextComponentDoc;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.EntityData;
import poussecafe.property.Property;

@Aggregate(
  factory = ServiceDocFactory.class,
  repository = ServiceDocRepository.class
)
public class ServiceDoc extends AggregateRoot<ServiceDocKey, ServiceDoc.Data> {

    void boundedContextComponentDoc(BoundedContextComponentDoc boundedContextComponentDoc) {
        Objects.requireNonNull(boundedContextComponentDoc);
        data().boundedContextComponentDoc().set(boundedContextComponentDoc);
    }

    public BoundedContextComponentDoc boundedContextComponentDoc() {
        return data().boundedContextComponentDoc().get();
    }

    public String id() {
        return StringNormalizer.normalizeString(boundedContextComponentDoc().componentDoc().name());
    }

    public static interface Data extends EntityData<ServiceDocKey> {

        Property<BoundedContextComponentDoc> boundedContextComponentDoc();
    }
}
