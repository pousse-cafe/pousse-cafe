package poussecafe.doc.model.entitydoc;

import java.io.Serializable;
import poussecafe.attribute.Attribute;
import poussecafe.doc.model.BoundedContextComponentDoc;
import poussecafe.doc.model.BoundedContextComponentDocData;

@SuppressWarnings("serial")
public class EntityDocData implements EntityDoc.Attributes, Serializable {

    @Override
    public Attribute<EntityDocId> identifier() {
        return new Attribute<EntityDocId>() {
            @Override
            public EntityDocId value() {
                return EntityDocId.ofClassName(className);
            }

            @Override
            public void value(EntityDocId value) {
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

    @Override
    public Attribute<String> idClassName() {
        return new Attribute<String>() {
            @Override
            public String value() {
                return idClassName;
            }

            @Override
            public void value(String value) {
                idClassName = value;
            }
        };
    }

    private String idClassName;
}
