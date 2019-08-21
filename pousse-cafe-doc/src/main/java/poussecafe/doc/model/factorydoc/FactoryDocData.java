package poussecafe.doc.model.factorydoc;

import java.io.Serializable;
import poussecafe.attribute.Attribute;
import poussecafe.doc.model.ModuleComponentDoc;
import poussecafe.doc.model.ModuleComponentDocData;

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
}
