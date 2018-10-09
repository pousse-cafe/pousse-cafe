package poussecafe.doc.model.factorydoc;

import java.util.List;
import java.util.Optional;
import poussecafe.doc.StringNormalizer;
import poussecafe.doc.model.BoundedContextComponentDoc;
import poussecafe.doc.model.step.StepDoc;
import poussecafe.doc.model.step.StepMethodSignature;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.EntityData;
import poussecafe.property.ListProperty;
import poussecafe.property.Property;

import static poussecafe.check.Checks.checkThatValue;

public class FactoryDoc extends AggregateRoot<FactoryDocKey, FactoryDoc.Data> {

    void boundedContextComponentDoc(BoundedContextComponentDoc boundedContextComponentDoc) {
        checkThatValue(boundedContextComponentDoc).notNull();
        getData().boundedContextComponentDoc().set(boundedContextComponentDoc);
    }

    public BoundedContextComponentDoc boundedContextComponentDoc() {
        return getData().boundedContextComponentDoc().get();
    }

    public String id() {
        return StringNormalizer.normalizeString(boundedContextComponentDoc().componentDoc().name());
    }

    void stepDocs(List<StepDoc> stepDocs) {
        getData().stepDocs().set(stepDocs);
    }

    public Optional<StepDoc> stepDocBySignature(StepMethodSignature methodSignature) {
        return getData()
                .stepDocs()
                .stream()
                .filter(stepDoc -> stepDoc.methodSignature().equals(methodSignature))
                .findFirst();
    }

    public List<StepDoc> stepDocs() {
        return getData().stepDocs().get();
    }

    public static interface Data extends EntityData<FactoryDocKey> {

        Property<BoundedContextComponentDoc> boundedContextComponentDoc();

        ListProperty<StepDoc> stepDocs();
    }
}
