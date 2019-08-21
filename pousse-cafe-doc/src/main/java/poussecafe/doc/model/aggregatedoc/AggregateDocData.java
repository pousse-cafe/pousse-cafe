package poussecafe.doc.model.aggregatedoc;

import java.io.Serializable;
import poussecafe.attribute.Attribute;
import poussecafe.doc.model.ModuleComponentDoc;
import poussecafe.doc.model.ModuleComponentDocData;

@SuppressWarnings("serial")
public class AggregateDocData implements AggregateDoc.Attributes, Serializable {

    @Override
    public Attribute<AggregateDocId> identifier() {
        return new Attribute<AggregateDocId>() {
            @Override
            public AggregateDocId value() {
                return AggregateDocId.ofClassName(className);
            }

            @Override
            public void value(AggregateDocId value) {
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
