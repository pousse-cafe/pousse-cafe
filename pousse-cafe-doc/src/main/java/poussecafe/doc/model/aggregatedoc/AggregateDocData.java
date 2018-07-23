package poussecafe.doc.model.aggregatedoc;

import java.io.Serializable;
import poussecafe.doc.model.BoundedContextComponentDoc;
import poussecafe.doc.model.BoundedContextComponentDocData;
import poussecafe.storable.Property;

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
}
