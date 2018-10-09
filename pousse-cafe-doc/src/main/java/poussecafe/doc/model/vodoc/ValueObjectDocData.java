package poussecafe.doc.model.vodoc;

import java.io.Serializable;
import poussecafe.doc.model.BoundedContextComponentDoc;
import poussecafe.doc.model.BoundedContextComponentDocData;
import poussecafe.property.Property;

@SuppressWarnings("serial")
public class ValueObjectDocData implements ValueObjectDoc.Data, Serializable {

    @Override
    public Property<ValueObjectDocKey> key() {
        return new Property<ValueObjectDocKey>() {
            @Override
            public ValueObjectDocKey get() {
                return ValueObjectDocKey.ofClassName(className);
            }

            @Override
            public void set(ValueObjectDocKey value) {
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
}
