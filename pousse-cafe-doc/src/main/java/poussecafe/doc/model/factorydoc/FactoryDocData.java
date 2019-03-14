package poussecafe.doc.model.factorydoc;

import java.io.Serializable;
import poussecafe.attribute.Attribute;
import poussecafe.doc.model.BoundedContextComponentDoc;
import poussecafe.doc.model.BoundedContextComponentDocData;

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
                return boundedContextComponentDoc.adapt();
            }

            @Override
            public void value(BoundedContextComponentDoc value) {
                boundedContextComponentDoc = BoundedContextComponentDocData.adapt(value);
            }
        };
    }

    private BoundedContextComponentDocData boundedContextComponentDoc;
}
