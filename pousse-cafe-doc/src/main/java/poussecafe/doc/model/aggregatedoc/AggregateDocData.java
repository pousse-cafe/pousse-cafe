package poussecafe.doc.model.aggregatedoc;

import java.io.Serializable;
import java.util.ArrayList;
import poussecafe.attribute.Attribute;
import poussecafe.attribute.ConvertingListAttribute;
import poussecafe.attribute.ListAttribute;
import poussecafe.doc.model.BoundedContextComponentDoc;
import poussecafe.doc.model.BoundedContextComponentDocData;
import poussecafe.doc.model.step.StepDoc;
import poussecafe.doc.model.step.StepDocData;

@SuppressWarnings("serial")
public class AggregateDocData implements AggregateDoc.Attributes, Serializable {

    @Override
    public Attribute<AggregateDocKey> key() {
        return new Attribute<AggregateDocKey>() {
            @Override
            public AggregateDocKey value() {
                return AggregateDocKey.ofClassName(className);
            }

            @Override
            public void value(AggregateDocKey value) {
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
                return componentDoc.toModel();
            }

            @Override
            public void value(BoundedContextComponentDoc value) {
                componentDoc = BoundedContextComponentDocData.of(value);
            }
        };
    }

    private BoundedContextComponentDocData componentDoc;

    @Override
    public Attribute<String> keyClassName() {
        return new Attribute<String>() {
            @Override
            public String value() {
                return keyClassName;
            }

            @Override
            public void value(String value) {
                keyClassName = value;
            }
        };
    }

    private String keyClassName;

    @Override
    public ListAttribute<StepDoc> stepDocs() {
        return new ConvertingListAttribute<StepDocData, StepDoc>(stepDocs) {
            @Override
            protected StepDoc convertFrom(StepDocData from) {
                return from.toModel();
            }

            @Override
            protected StepDocData convertTo(StepDoc from) {
                return StepDocData.of(from);
            }
        };
    }

    private ArrayList<StepDocData> stepDocs = new ArrayList<>();
}
