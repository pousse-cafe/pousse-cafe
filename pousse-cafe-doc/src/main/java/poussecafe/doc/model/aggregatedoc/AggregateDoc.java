package poussecafe.doc.model.aggregatedoc;

import java.util.List;
import java.util.Optional;
import poussecafe.attribute.Attribute;
import poussecafe.attribute.ListAttribute;
import poussecafe.contextconfigurer.Aggregate;
import poussecafe.doc.StringNormalizer;
import poussecafe.doc.model.BoundedContextComponentDoc;
import poussecafe.doc.model.step.StepDoc;
import poussecafe.doc.model.step.StepMethodSignature;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.EntityAttributes;

@Aggregate(
    factory = AggregateDocFactory.class,
    repository = AggregateDocRepository.class
)
public class AggregateDoc extends AggregateRoot<AggregateDocKey, AggregateDoc.Attributes> {

    public String id() {
        return StringNormalizer.normalizeString(attributes().boundedContextComponentDoc().value().componentDoc().name());
    }

    public String className() {
        return attributes().key().value().getValue();
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

    public static interface Attributes extends EntityAttributes<AggregateDocKey> {

        Attribute<BoundedContextComponentDoc> boundedContextComponentDoc();

        Attribute<String> keyClassName();

        ListAttribute<StepDoc> stepDocs();
    }
}
