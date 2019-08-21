package poussecafe.doc.model.moduledoc;

import java.io.Serializable;
import poussecafe.attribute.Attribute;
import poussecafe.doc.model.ComponentDoc;
import poussecafe.doc.model.ComponentDocData;

@SuppressWarnings("serial")
public class ModuleDocData implements ModuleDoc.Attributes, Serializable {

    @Override
    public Attribute<ModuleDocId> identifier() {
        return new Attribute<ModuleDocId>() {
            @Override
            public ModuleDocId value() {
                return ModuleDocId.ofPackageName(id);
            }

            @Override
            public void value(ModuleDocId value) {
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
