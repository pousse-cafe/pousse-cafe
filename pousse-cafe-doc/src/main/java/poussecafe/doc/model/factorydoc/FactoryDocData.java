package poussecafe.doc.model.factorydoc;

import java.io.Serializable;
import poussecafe.attribute.Attribute;
import poussecafe.doc.model.BoundedContextComponentDoc;
import poussecafe.doc.model.BoundedContextComponentDocData;

@SuppressWarnings("serial")
public class FactoryDocData implements FactoryDoc.Attributes, Serializable {

    @Override
    public Attribute<FactoryDocId> identifier() {
        return new Attribute<FactoryDocId>() {
            @Override
            public FactoryDocId value() {
                return FactoryDocId.ofClassName(className);
            }

            @Override
            public void value(FactoryDocId value) {
                className = value.stringValue();
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
