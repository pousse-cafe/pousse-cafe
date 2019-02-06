package poussecafe.doc.model.entitydoc;

import java.io.Serializable;
import poussecafe.attribute.Attribute;
import poussecafe.doc.model.BoundedContextComponentDoc;
import poussecafe.doc.model.BoundedContextComponentDocData;

@SuppressWarnings("serial")
public class EntityDocData implements EntityDoc.Attributes, Serializable {

    @Override
    public Attribute<EntityDocKey> key() {
        return new Attribute<EntityDocKey>() {
            @Override
            public EntityDocKey value() {
                return EntityDocKey.ofClassName(className);
            }

            @Override
            public void value(EntityDocKey value) {
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
