package poussecafe.doc.model.servicedoc;

import poussecafe.attribute.Attribute;
import poussecafe.discovery.Aggregate;
import poussecafe.doc.StringNormalizer;
import poussecafe.doc.model.BoundedContextComponentDoc;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.EntityAttributes;

@Aggregate(
  factory = ServiceDocFactory.class,
  repository = ServiceDocRepository.class
)
public class ServiceDoc extends AggregateRoot<ServiceDocId, ServiceDoc.Attributes> {

    public String id() {
        return StringNormalizer.normalizeString(attributes().boundedContextComponentDoc().value().componentDoc().name());
    }

    public static interface Attributes extends EntityAttributes<ServiceDocId> {

        Attribute<BoundedContextComponentDoc> boundedContextComponentDoc();
    }
}
