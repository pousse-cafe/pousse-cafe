package poussecafe.doc.model.entitydoc;

import java.io.Serializable;
import poussecafe.attribute.Attribute;
import poussecafe.doc.model.ModuleComponentDoc;
import poussecafe.doc.model.ModuleComponentDocData;

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
    public Attribute<ModuleComponentDoc> moduleComponentDoc() {
        return new Attribute<ModuleComponentDoc>() {
            @Override
            public ModuleComponentDoc value() {
                return moduleComponentDoc.adapt();
            }

            @Override
            public void value(ModuleComponentDoc value) {
                moduleComponentDoc = ModuleComponentDocData.adapt(value);
            }
        };
    }

    private ModuleComponentDocData moduleComponentDoc;

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
