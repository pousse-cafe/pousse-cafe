package poussecafe.doc.model.vodoc;

import java.io.Serializable;
import poussecafe.attribute.Attribute;
import poussecafe.doc.model.ModuleComponentDoc;
import poussecafe.doc.model.ModuleComponentDocData;

@SuppressWarnings("serial")
public class ValueObjectDocData implements ValueObjectDoc.Attributes, Serializable {

    @Override
    public Attribute<ValueObjectDocId> identifier() {
        return new Attribute<ValueObjectDocId>() {
            @Override
            public ValueObjectDocId value() {
                return ValueObjectDocId.ofClassName(className);
            }

            @Override
            public void value(ValueObjectDocId value) {
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
                return componentDoc.adapt();
            }

            @Override
            public void value(ModuleComponentDoc value) {
                componentDoc = ModuleComponentDocData.adapt(value);
            }
        };
    }

    private ModuleComponentDocData componentDoc;
}
