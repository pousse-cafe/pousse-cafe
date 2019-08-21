package poussecafe.doc.model.domainprocessdoc;

import java.io.Serializable;
import poussecafe.attribute.Attribute;
import poussecafe.doc.model.ModuleComponentDoc;
import poussecafe.doc.model.ModuleComponentDocData;

@SuppressWarnings("serial")
public class DomainProcessDocData implements DomainProcessDoc.Attributes, Serializable {

    @Override
    public Attribute<DomainProcessDocId> identifier() {
        return new Attribute<DomainProcessDocId>() {
            @Override
            public DomainProcessDocId value() {
                return new DomainProcessDocId(className);
            }

            @Override
            public void value(DomainProcessDocId value) {
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
