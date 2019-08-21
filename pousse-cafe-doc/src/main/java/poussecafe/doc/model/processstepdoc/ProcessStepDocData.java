package poussecafe.doc.model.processstepdoc;

import java.io.Serializable;
import java.util.HashSet;
import poussecafe.attribute.Attribute;
import poussecafe.attribute.AttributeBuilder;
import poussecafe.attribute.OptionalAttribute;
import poussecafe.attribute.SetAttribute;
import poussecafe.doc.model.ModuleComponentDoc;
import poussecafe.doc.model.ModuleComponentDocData;

@SuppressWarnings("serial")
public class ProcessStepDocData implements Serializable, ProcessStepDoc.Attributes {

    @Override
    public Attribute<ProcessStepDocId> identifier() {
        return AttributeBuilder.stringId(ProcessStepDocId.class)
                .read(() -> id)
                .write(value -> id = value)
                .build();
    }

    private String id;

    @Override
    public Attribute<ModuleComponentDoc> moduleComponentDoc() {
        return AttributeBuilder.single(ModuleComponentDoc.class)
                .usingAutoAdapter(ModuleComponentDocData.class)
                .read(() -> moduleComponentDoc)
                .write(value -> moduleComponentDoc = value)
                .build();
    }

    private ModuleComponentDocData moduleComponentDoc;

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
                .read(() -> processName)
                .write(value -> processName = value)
                .build();
    }

    private String processName;

    @Override
    public OptionalAttribute<StepMethodSignature> stepMethodSignature() {
        return AttributeBuilder.optional(StepMethodSignature.class)
                .usingAutoAdapter(StepMethodSignatureData.class)
                .read(() -> stepMethodSignature)
                .write(value -> stepMethodSignature = value)
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
