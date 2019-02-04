package poussecafe.doc.model.servicedoc;

import poussecafe.contextconfigurer.Aggregate;
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

    public String id() {
        return StringNormalizer.normalizeString(data().boundedContextComponentDoc().get().componentDoc().name());
    }

    public static interface Data extends EntityData<ServiceDocKey> {

        Property<BoundedContextComponentDoc> boundedContextComponentDoc();
    }
}
