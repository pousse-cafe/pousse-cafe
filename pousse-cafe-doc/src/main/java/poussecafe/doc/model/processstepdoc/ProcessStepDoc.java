package poussecafe.doc.model.processstepdoc;

import poussecafe.attribute.Attribute;
import poussecafe.attribute.ListAttribute;
import poussecafe.discovery.Aggregate;
import poussecafe.doc.model.BoundedContextComponentDoc;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.EntityAttributes;

@Aggregate(
    factory = ProcessStepDocFactory.class,
    repository = ProcessStepDocRepository.class
)
public class ProcessStepDoc extends AggregateRoot<ProcessStepDocKey, ProcessStepDoc.Attributes> {

    public static interface Attributes extends EntityAttributes<ProcessStepDocKey> {

        Attribute<BoundedContextComponentDoc> boundedContextComponentDoc();

        Attribute<StepMethodSignature> stepMethodSignature();

        ListAttribute<String> producedEvents();
    }
}
