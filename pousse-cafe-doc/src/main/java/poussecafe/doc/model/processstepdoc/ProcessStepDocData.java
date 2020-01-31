package poussecafe.doc.model.processstepdoc;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import poussecafe.attribute.Attribute;
import poussecafe.attribute.AttributeBuilder;
import poussecafe.attribute.MapAttribute;
import poussecafe.attribute.OptionalAttribute;
import poussecafe.attribute.SetAttribute;
import poussecafe.attribute.adapters.DataAdapters;
import poussecafe.doc.model.ModuleComponentDoc;
import poussecafe.doc.model.ModuleComponentDocData;
import poussecafe.doc.model.aggregatedoc.AggregateDocId;
import poussecafe.doc.model.aggregatedoc.adapters.AggregateDocIdDataAdapter;

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
    public SetAttribute<String> processNames() {
        return AttributeBuilder.set(String.class)
                .withSet(processNames)
                .build();
    }

    private HashSet<String> processNames = new HashSet<>();

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

    @Override
    public OptionalAttribute<AggregateDocId> aggregate() {
        return AttributeBuilder.optional(AggregateDocId.class)
                .usingDataAdapter(AggregateDocIdDataAdapter.instance())
                .read(() -> aggregateClassName)
                .write(value -> aggregateClassName = value)
                .build();
    }

    private String aggregateClassName;

    @Override
    public MapAttribute<String, List<String>> toExternalsByEvent() {
        return AttributeBuilder.map(String.class, DataAdapters.parametrizedListClass(String.class))
                .usingEntryDataAdapters(DataAdapters.identity(),
                        DataAdapters.listWithAdapter(DataAdapters.identity()))
                .withMap(toExternalsByEvent)
                .build();
    }

    private HashMap<String, List<String>> toExternalsByEvent = new HashMap<>();
}
