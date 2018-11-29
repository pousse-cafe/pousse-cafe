package poussecafe.doc.model.boundedcontextdoc;

import java.io.Serializable;
import poussecafe.doc.model.ComponentDoc;
import poussecafe.doc.model.ComponentDocData;
import poussecafe.property.Property;

@SuppressWarnings("serial")
public class BoundedContextDocData implements BoundedContextDoc.Data, Serializable {

    @Override
    public Property<BoundedContextDocKey> key() {
        return new Property<BoundedContextDocKey>() {
            @Override
            public BoundedContextDocKey get() {
                return BoundedContextDocKey.ofPackageName(id);
            }

            @Override
            public void set(BoundedContextDocKey value) {
                id = value.getValue();
            }
        };
    }

    private String id;

    @Override
    public Property<ComponentDoc> componentDoc() {
        return new Property<ComponentDoc>() {
            @Override
            public ComponentDoc get() {
                return componentDoc.toModel();
            }

            @Override
            public void set(ComponentDoc value) {
                componentDoc = ComponentDocData.of(value);
            }
        };
    }

    private ComponentDocData componentDoc;
}
