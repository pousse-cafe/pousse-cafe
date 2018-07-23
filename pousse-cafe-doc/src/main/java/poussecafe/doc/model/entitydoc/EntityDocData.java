package poussecafe.doc.model.entitydoc;

import java.io.Serializable;
import poussecafe.doc.model.BoundedContextComponentDoc;
import poussecafe.doc.model.BoundedContextComponentDocData;
import poussecafe.storable.Property;

@SuppressWarnings("serial")
public class EntityDocData implements EntityDoc.Data, Serializable {

    @Override
    public Property<EntityDocKey> key() {
        return new Property<EntityDocKey>() {
            @Override
            public EntityDocKey get() {
                return EntityDocKey.ofClassName(className);
            }

            @Override
            public void set(EntityDocKey value) {
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
                return boundedContextComponentDoc.toModel();
            }

            @Override
            public void set(BoundedContextComponentDoc value) {
                boundedContextComponentDoc = BoundedContextComponentDocData.of(value);
            }
        };
    }

    private BoundedContextComponentDocData boundedContextComponentDoc;
}
