package poussecafe.doc.model.aggregatedoc;

import poussecafe.attribute.Attribute;
import poussecafe.discovery.Aggregate;
import poussecafe.doc.StringNormalizer;
import poussecafe.doc.model.BoundedContextComponentDoc;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.EntityAttributes;

@Aggregate(
    factory = AggregateDocFactory.class,
    repository = AggregateDocRepository.class
)
public class AggregateDoc extends AggregateRoot<AggregateDocId, AggregateDoc.Attributes> {

    public String id() {
        return StringNormalizer.normalizeString(attributes().boundedContextComponentDoc().value().componentDoc().name());
    }

    public String className() {
        return attributes().identifier().value().getValue();
    }

    public static interface Attributes extends EntityAttributes<AggregateDocId> {

        Attribute<BoundedContextComponentDoc> boundedContextComponentDoc();

        Attribute<String> idClassName();
    }
}
