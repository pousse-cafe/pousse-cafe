package poussecafe.doc.model.aggregatedoc;

import java.io.Serializable;
import poussecafe.attribute.Attribute;
import poussecafe.doc.model.BoundedContextComponentDoc;
import poussecafe.doc.model.BoundedContextComponentDocData;

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
}
