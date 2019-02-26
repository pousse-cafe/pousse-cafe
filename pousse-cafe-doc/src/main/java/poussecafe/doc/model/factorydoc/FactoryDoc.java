package poussecafe.doc.model.factorydoc;

import java.util.List;
import java.util.Optional;
import poussecafe.attribute.Attribute;
import poussecafe.attribute.ListAttribute;
import poussecafe.discovery.Aggregate;
import poussecafe.doc.StringNormalizer;
import poussecafe.doc.model.BoundedContextComponentDoc;
import poussecafe.doc.model.step.StepDoc;
import poussecafe.doc.model.step.StepMethodSignature;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.EntityAttributes;

@Aggregate(
  factory = FactoryDocFactory.class,
  repository = FactoryDocRepository.class
)
public class FactoryDoc extends AggregateRoot<FactoryDocKey, FactoryDoc.Attributes> {

    public String id() {
        return StringNormalizer.normalizeString(attributes().boundedContextComponentDoc().value().componentDoc().name());
    }

    void stepDocs(List<StepDoc> stepDocs) {
        attributes().stepDocs().value(stepDocs);
    }

    public Optional<StepDoc> stepDocBySignature(StepMethodSignature methodSignature) {
        return attributes()
                .stepDocs()
                .stream()
                .filter(stepDoc -> stepDoc.methodSignature().equals(methodSignature))
                .findFirst();
    }

    public static interface Attributes extends EntityAttributes<FactoryDocKey> {

        Attribute<BoundedContextComponentDoc> boundedContextComponentDoc();

        ListAttribute<StepDoc> stepDocs();
    }
}
