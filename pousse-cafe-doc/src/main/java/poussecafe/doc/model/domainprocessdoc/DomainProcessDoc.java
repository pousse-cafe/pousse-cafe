package poussecafe.doc.model.domainprocessdoc;

import java.util.List;
import poussecafe.attribute.Attribute;
import poussecafe.attribute.ListAttribute;
import poussecafe.attribute.MapAttribute;
import poussecafe.discovery.Aggregate;
import poussecafe.doc.StringNormalizer;
import poussecafe.doc.model.BoundedContextComponentDoc;
import poussecafe.doc.model.processstepdoc.StepMethodSignature;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.EntityAttributes;

@Aggregate(
  factory = DomainProcessDocFactory.class,
  repository = DomainProcessDocRepository.class
)
public class DomainProcessDoc extends AggregateRoot<DomainProcessDocKey, DomainProcessDoc.Attributes> {

    public String id() {
        return StringNormalizer.normalizeString(attributes().boundedContextComponentDoc().value().componentDoc().name());
    }

    public static interface Attributes extends EntityAttributes<DomainProcessDocKey> {

        Attribute<BoundedContextComponentDoc> boundedContextComponentDoc();

        ListAttribute<StepMethodSignature> steps();

        MapAttribute<StepName, List<StepName>> toExternals();

        MapAttribute<StepName, List<StepName>> fromExternals();
    }
}
