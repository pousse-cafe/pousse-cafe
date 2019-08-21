package poussecafe.doc.model.aggregatedoc;

import poussecafe.attribute.Attribute;
import poussecafe.discovery.Aggregate;
import poussecafe.doc.StringNormalizer;
import poussecafe.doc.model.ModuleComponentDoc;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.EntityAttributes;

@Aggregate(
    factory = AggregateDocFactory.class,
    repository = AggregateDocRepository.class
)
public class AggregateDoc extends AggregateRoot<AggregateDocId, AggregateDoc.Attributes> {

    public String id() {
        return StringNormalizer.normalizeString(attributes().moduleComponentDoc().value().componentDoc().name());
    }

    public String className() {
        return attributes().identifier().value().stringValue();
    }

    public static interface Attributes extends EntityAttributes<AggregateDocId> {

        Attribute<ModuleComponentDoc> moduleComponentDoc();

        Attribute<String> idClassName();
    }
}
