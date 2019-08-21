package poussecafe.doc.model.servicedoc;

import java.io.Serializable;
import poussecafe.attribute.Attribute;
import poussecafe.doc.model.ModuleComponentDoc;
import poussecafe.doc.model.ModuleComponentDocData;

@SuppressWarnings("serial")
public class ServiceDocData implements ServiceDoc.Attributes, Serializable {

    @Override
    public Attribute<ServiceDocId> identifier() {
        return new Attribute<ServiceDocId>() {
            @Override
            public ServiceDocId value() {
                return ServiceDocId.ofClassName(className);
            }

            @Override
            public void value(ServiceDocId value) {
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
