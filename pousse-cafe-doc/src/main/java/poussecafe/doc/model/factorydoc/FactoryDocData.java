package poussecafe.doc.model.factorydoc;

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
public class FactoryDocData implements FactoryDoc.Attributes, Serializable {

    @Override
    public Attribute<FactoryDocKey> key() {
        return new Attribute<FactoryDocKey>() {
            @Override
            public FactoryDocKey value() {
                return FactoryDocKey.ofClassName(className);
            }

            @Override
            public void value(FactoryDocKey value) {
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
                return boundedContextComponentDoc.toModel();
            }

            @Override
            public void value(BoundedContextComponentDoc value) {
                boundedContextComponentDoc = BoundedContextComponentDocData.of(value);
            }
        };
    }

    private BoundedContextComponentDocData boundedContextComponentDoc;

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
