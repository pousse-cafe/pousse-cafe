package poussecafe.doc.model.domainprocessdoc;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import poussecafe.attribute.Attribute;
import poussecafe.attribute.AttributeBuilder;
import poussecafe.attribute.ListAttribute;
import poussecafe.attribute.MapAttribute;
import poussecafe.attribute.adapters.DataAdapters;
import poussecafe.doc.model.BoundedContextComponentDoc;
import poussecafe.doc.model.BoundedContextComponentDocData;
import poussecafe.doc.model.messagelistenerdoc.StepMethodSignature;
import poussecafe.doc.model.messagelistenerdoc.StepMethodSignatureData;

@SuppressWarnings("serial")
public class DomainProcessDocData implements DomainProcessDoc.Attributes, Serializable {

    @Override
    public Attribute<DomainProcessDocKey> key() {
        return new Attribute<DomainProcessDocKey>() {
            @Override
            public DomainProcessDocKey value() {
                return DomainProcessDocKey.ofClassName(className);
            }

            @Override
            public void value(DomainProcessDocKey value) {
                className = value.getValue();
            }
        };
    }

    private String className;

    @Override
    public Attribute<BoundedContextComponentDoc> boundedContextComponentDoc() {
        return new Attribute<BoundedContextComponentDoc>() {
            @Override
            public BoundedContextComponentDoc value() {
                return componentDoc.adapt();
            }

            @Override
            public void value(BoundedContextComponentDoc value) {
                componentDoc = BoundedContextComponentDocData.adapt(value);
            }
        };
    }

    private BoundedContextComponentDocData componentDoc;

    @Override
    public ListAttribute<StepMethodSignature> steps() {
        return AttributeBuilder.list(StepMethodSignature.class)
                .fromAutoAdapting(StepMethodSignatureData.class)
                .withList(steps)
                .build();
    }

    private ArrayList<StepMethodSignatureData> steps = new ArrayList<>();

    @Override
    public MapAttribute<StepName, List<StepName>> toExternals() {
        return AttributeBuilder.map(StepName.class, DataAdapters.parametrizedListClass(StepName.class))
                .fromAdapting(StepNameAdapter.instance(), DataAdapters.listWithAdapter(StepNameAdapter.instance()))
                .withMap(toExternals)
                .build();
    }

    private HashMap<String, List<String>> toExternals = new HashMap<>();

    @Override
    public MapAttribute<StepName, List<StepName>> fromExternals() {
        return AttributeBuilder.map(StepName.class, DataAdapters.parametrizedListClass(StepName.class))
                .fromAdapting(StepNameAdapter.instance(), DataAdapters.listWithAdapter(StepNameAdapter.instance()))
                .withMap(fromExternals)
                .build();
    }

    private HashMap<String, List<String>> fromExternals = new HashMap<>();
}
