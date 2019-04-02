package poussecafe.doc.model.processstepdoc;

import java.io.Serializable;
import java.util.HashSet;
import poussecafe.attribute.Attribute;
import poussecafe.attribute.AttributeBuilder;
import poussecafe.attribute.OptionalAttribute;
import poussecafe.attribute.SetAttribute;
import poussecafe.doc.model.BoundedContextComponentDoc;
import poussecafe.doc.model.BoundedContextComponentDocData;

@SuppressWarnings("serial")
public class ProcessStepDocData implements Serializable, ProcessStepDoc.Attributes {

    @Override
    public Attribute<ProcessStepDocId> identifier() {
        return AttributeBuilder.stringId(ProcessStepDocId.class)
                .get(() -> id)
                .set(value -> id = value)
                .build();
    }

    private String id;

    @Override
    public Attribute<BoundedContextComponentDoc> boundedContextComponentDoc() {
        return AttributeBuilder.single(BoundedContextComponentDoc.class)
                .fromAutoAdapting(BoundedContextComponentDocData.class)
                .get(() -> boundedContextComponentDoc)
                .set(value -> boundedContextComponentDoc = value)
                .build();
    }

    private BoundedContextComponentDocData boundedContextComponentDoc;

    @Override
    public SetAttribute<String> producedEvents() {
        return AttributeBuilder.set(String.class)
                .withSet(producedEvents)
                .build();
    }

    private HashSet<String> producedEvents = new HashSet<>();

    @Override
    public OptionalAttribute<String> processName() {
        return AttributeBuilder.optional(String.class)
                .get(() -> processName)
                .set(value -> processName = value)
                .build();
    }

    private String processName;

    @Override
    public OptionalAttribute<StepMethodSignature> stepMethodSignature() {
        return AttributeBuilder.optional(StepMethodSignature.class)
                .fromAutoAdapting(StepMethodSignatureData.class)
                .get(() -> stepMethodSignature)
                .set(value -> stepMethodSignature = value)
                .build();
    }

    private StepMethodSignatureData stepMethodSignature;

    @Override
    public SetAttribute<String> toExternals() {
        return AttributeBuilder.set(String.class)
                .withSet(toExternals)
                .build();
    }

    private HashSet<String> toExternals = new HashSet<>();

    @Override
    public SetAttribute<String> fromExternals() {
        return AttributeBuilder.set(String.class)
                .withSet(fromExternals)
                .build();
    }

    private HashSet<String> fromExternals = new HashSet<>();
}
