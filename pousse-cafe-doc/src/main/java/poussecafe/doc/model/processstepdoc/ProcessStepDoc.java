package poussecafe.doc.model.processstepdoc;

import poussecafe.attribute.Attribute;
import poussecafe.attribute.OptionalAttribute;
import poussecafe.attribute.SetAttribute;
import poussecafe.discovery.Aggregate;
import poussecafe.doc.model.BoundedContextComponentDoc;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.EntityAttributes;

@Aggregate(
    factory = ProcessStepDocFactory.class,
    repository = ProcessStepDocRepository.class
)
public class ProcessStepDoc extends AggregateRoot<ProcessStepDocId, ProcessStepDoc.Attributes> {

    public static interface Attributes extends EntityAttributes<ProcessStepDocId> {

        Attribute<BoundedContextComponentDoc> boundedContextComponentDoc();

        OptionalAttribute<String> processName();

        OptionalAttribute<StepMethodSignature> stepMethodSignature();

        SetAttribute<String> producedEvents();

        SetAttribute<String> toExternals();

        SetAttribute<String> fromExternals();
    }
}
