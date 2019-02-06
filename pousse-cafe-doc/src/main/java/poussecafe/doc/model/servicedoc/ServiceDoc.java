package poussecafe.doc.model.servicedoc;

import poussecafe.attribute.Attribute;
import poussecafe.contextconfigurer.Aggregate;
import poussecafe.doc.StringNormalizer;
import poussecafe.doc.model.BoundedContextComponentDoc;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.EntityAttributes;

@Aggregate(
  factory = ServiceDocFactory.class,
  repository = ServiceDocRepository.class
)
public class ServiceDoc extends AggregateRoot<ServiceDocKey, ServiceDoc.Attributes> {

    public String id() {
        return StringNormalizer.normalizeString(attributes().boundedContextComponentDoc().value().componentDoc().name());
    }

    public static interface Attributes extends EntityAttributes<ServiceDocKey> {

        Attribute<BoundedContextComponentDoc> boundedContextComponentDoc();
    }
}
