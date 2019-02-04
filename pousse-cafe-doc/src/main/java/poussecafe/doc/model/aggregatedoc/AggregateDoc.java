package poussecafe.doc.model.aggregatedoc;

import java.util.List;
import java.util.Optional;
import poussecafe.contextconfigurer.Aggregate;
import poussecafe.doc.StringNormalizer;
import poussecafe.doc.model.BoundedContextComponentDoc;
import poussecafe.doc.model.step.StepDoc;
import poussecafe.doc.model.step.StepMethodSignature;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.EntityData;
import poussecafe.property.ListProperty;
import poussecafe.property.Property;

@Aggregate(
    factory = AggregateDocFactory.class,
    repository = AggregateDocRepository.class
)
public class AggregateDoc extends AggregateRoot<AggregateDocKey, AggregateDoc.Data> {

    public String id() {
        return StringNormalizer.normalizeString(data().boundedContextComponentDoc().get().componentDoc().name());
    }

    public String className() {
        return data().key().get().getValue();
    }

    void stepDocs(List<StepDoc> stepDocs) {
        data().stepDocs().set(stepDocs);
    }

    public Optional<StepDoc> stepDocBySignature(StepMethodSignature methodSignature) {
        return data()
                .stepDocs()
                .stream()
                .filter(stepDoc -> stepDoc.methodSignature().equals(methodSignature))
                .findFirst();
    }

    public static interface Data extends EntityData<AggregateDocKey> {

        Property<BoundedContextComponentDoc> boundedContextComponentDoc();

        Property<String> keyClassName();

        ListProperty<StepDoc> stepDocs();
    }
}
