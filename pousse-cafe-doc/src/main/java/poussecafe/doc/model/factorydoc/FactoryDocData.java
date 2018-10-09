package poussecafe.doc.model.factorydoc;

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
public class FactoryDocData implements FactoryDoc.Data, Serializable {

    @Override
    public Property<FactoryDocKey> key() {
        return new Property<FactoryDocKey>() {
            @Override
            public FactoryDocKey get() {
                return FactoryDocKey.ofClassName(className);
            }

            @Override
            public void set(FactoryDocKey value) {
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
                return boundedContextComponentDoc.toModel();
            }

            @Override
            public void set(BoundedContextComponentDoc value) {
                boundedContextComponentDoc = BoundedContextComponentDocData.of(value);
            }
        };
    }

    private BoundedContextComponentDocData boundedContextComponentDoc;

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
