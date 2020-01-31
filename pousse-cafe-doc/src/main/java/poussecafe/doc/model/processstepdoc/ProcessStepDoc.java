package poussecafe.doc.model.processstepdoc;

import java.util.List;
import poussecafe.attribute.Attribute;
import poussecafe.attribute.MapAttribute;
import poussecafe.attribute.OptionalAttribute;
import poussecafe.attribute.SetAttribute;
import poussecafe.discovery.Aggregate;
import poussecafe.doc.model.ModuleComponentDoc;
import poussecafe.doc.model.aggregatedoc.AggregateDocId;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.EntityAttributes;

@Aggregate(
    factory = ProcessStepDocFactory.class,
    repository = ProcessStepDocRepository.class
)
public class ProcessStepDoc extends AggregateRoot<ProcessStepDocId, ProcessStepDoc.Attributes> {

    public static interface Attributes extends EntityAttributes<ProcessStepDocId> {

        Attribute<ModuleComponentDoc> moduleComponentDoc();

        SetAttribute<String> processNames();

        OptionalAttribute<StepMethodSignature> stepMethodSignature();

        SetAttribute<String> producedEvents();

        SetAttribute<String> toExternals();

        MapAttribute<String, List<String>> toExternalsByEvent();

        SetAttribute<String> fromExternals();

        OptionalAttribute<AggregateDocId> aggregate();
    }
}
