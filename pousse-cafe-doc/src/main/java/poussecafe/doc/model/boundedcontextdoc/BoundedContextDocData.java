package poussecafe.doc.model.boundedcontextdoc;

import java.io.Serializable;
import poussecafe.attribute.Attribute;
import poussecafe.doc.model.ComponentDoc;
import poussecafe.doc.model.ComponentDocData;

@SuppressWarnings("serial")
public class BoundedContextDocData implements BoundedContextDoc.Attributes, Serializable {

    @Override
    public Attribute<BoundedContextDocKey> key() {
        return new Attribute<BoundedContextDocKey>() {
            @Override
            public BoundedContextDocKey value() {
                return BoundedContextDocKey.ofPackageName(id);
            }

            @Override
            public void value(BoundedContextDocKey value) {
                id = value.getValue();
            }
        };
    }

    private String id;

    @Override
    public Attribute<ComponentDoc> componentDoc() {
        return new Attribute<ComponentDoc>() {
            @Override
            public ComponentDoc value() {
                return componentDoc.toModel();
            }

            @Override
            public void value(ComponentDoc value) {
                componentDoc = ComponentDocData.of(value);
            }
        };
    }

    private ComponentDocData componentDoc;
}
