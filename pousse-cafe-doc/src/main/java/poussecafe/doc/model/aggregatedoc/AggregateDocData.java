package poussecafe.doc.model.aggregatedoc;

import java.io.Serializable;
import java.util.ArrayList;
import poussecafe.doc.model.BoundedContextComponentDoc;
import poussecafe.doc.model.BoundedContextComponentDocData;
import poussecafe.doc.model.step.StepDoc;
import poussecafe.doc.model.step.StepDocData;
import poussecafe.property.ConvertingListProperty;
import poussecafe.property.ListProperty;
import poussecafe.property.Property;

@SuppressWarnings("serial")
public class AggregateDocData implements AggregateDoc.Data, Serializable {

    @Override
    public Property<AggregateDocKey> key() {
        return new Property<AggregateDocKey>() {
            @Override
            public AggregateDocKey get() {
                return AggregateDocKey.ofClassName(className);
            }

            @Override
            public void set(AggregateDocKey value) {
                className = value.getValue();
            }
        };
    }

    private String className;

    @Override
    public Property<BoundedContextComponentDoc> boundedContextComponentDoc() {
        return new Property<BoundedContextComponentDoc>() {
            @Override
            public BoundedContextComponentDoc get() {
                return componentDoc.toModel();
            }

            @Override
            public void set(BoundedContextComponentDoc value) {
                componentDoc = BoundedContextComponentDocData.of(value);
            }
        };
    }

    private BoundedContextComponentDocData componentDoc;

    @Override
    public Property<String> keyClassName() {
        return new Property<String>() {
            @Override
            public String get() {
                return keyClassName;
            }

            @Override
            public void set(String value) {
                keyClassName = value;
            }
        };
    }

    private String keyClassName;

    @Override
    public ListProperty<StepDoc> stepDocs() {
        return new ConvertingListProperty<StepDocData, StepDoc>(stepDocs) {
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
