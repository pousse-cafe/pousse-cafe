package poussecafe.doc.model.factorydoc;

import poussecafe.attribute.Attribute;
import poussecafe.discovery.Aggregate;
import poussecafe.doc.StringNormalizer;
import poussecafe.doc.model.BoundedContextComponentDoc;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.EntityAttributes;

@Aggregate(
  factory = FactoryDocFactory.class,
  repository = FactoryDocRepository.class
)
public class FactoryDoc extends AggregateRoot<FactoryDocId, FactoryDoc.Attributes> {

    public String id() {
        return StringNormalizer.normalizeString(attributes().boundedContextComponentDoc().value().componentDoc().name());
    }

    public static interface Attributes extends EntityAttributes<FactoryDocId> {

        Attribute<BoundedContextComponentDoc> boundedContextComponentDoc();
    }
}
