package poussecafe.doc.model.domainprocessdoc;

import java.io.Serializable;
import java.util.HashMap;
import poussecafe.doc.model.BoundedContextComponentDoc;
import poussecafe.doc.model.BoundedContextComponentDocData;
import poussecafe.storable.MapProperty;
import poussecafe.storable.Property;
import poussecafe.storage.memory.ConvertingMapProperty;

@SuppressWarnings("serial")
public class DomainProcessDocData implements DomainProcessDoc.Data, Serializable {

    @Override
    public Property<DomainProcessDocKey> key() {
        return new Property<DomainProcessDocKey>() {
            @Override
            public DomainProcessDocKey get() {
                return DomainProcessDocKey.ofClassName(className);
            }

            @Override
            public void set(DomainProcessDocKey value) {
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
    public MapProperty<String, Step> steps() {
        return new ConvertingMapProperty<String, StepData, String, Step>(steps) {
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
