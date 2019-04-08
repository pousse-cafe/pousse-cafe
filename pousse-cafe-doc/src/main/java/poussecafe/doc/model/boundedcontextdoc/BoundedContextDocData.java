package poussecafe.doc.model.boundedcontextdoc;

import java.io.Serializable;
import poussecafe.attribute.Attribute;
import poussecafe.doc.model.ComponentDoc;
import poussecafe.doc.model.ComponentDocData;

@SuppressWarnings("serial")
public class BoundedContextDocData implements BoundedContextDoc.Attributes, Serializable {

    @Override
    public Attribute<BoundedContextDocId> identifier() {
        return new Attribute<BoundedContextDocId>() {
            @Override
            public BoundedContextDocId value() {
                return BoundedContextDocId.ofPackageName(id);
            }

            @Override
            public void value(BoundedContextDocId value) {
                id = value.stringValue();
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
