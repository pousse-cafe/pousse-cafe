package poussecafe.doc.model.domainprocessdoc;

import java.io.Serializable;
import java.util.HashMap;
import poussecafe.attribute.Attribute;
import poussecafe.attribute.ConvertingMapAttribute;
import poussecafe.attribute.MapAttribute;
import poussecafe.doc.model.BoundedContextComponentDoc;
import poussecafe.doc.model.BoundedContextComponentDocData;

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
    public MapAttribute<String, Step> steps() {
        return new ConvertingMapAttribute<String, StepData, String, Step>(steps) {
            @Override
            protected String convertFromKey(String from) {
                return from;
            }

            @Override
            protected Step convertFromValue(StepData from) {
                return from.toModel();
            }

            @Override
            protected String convertToKey(String from) {
                return from;
            }

            @Override
            protected StepData convertToValue(Step from) {
                return StepData.of(from);
            }
        };
    }

    private HashMap<String, StepData> steps = new HashMap<>();
}
