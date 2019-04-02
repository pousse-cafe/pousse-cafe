package poussecafe.doc.model.domainprocessdoc;

import java.io.Serializable;
import poussecafe.attribute.Attribute;
import poussecafe.doc.model.BoundedContextComponentDoc;
import poussecafe.doc.model.BoundedContextComponentDocData;

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
                className = value.getValue();
            }
        };
    }

    private String className;

    @Override
    public Attribute<BoundedContextComponentDoc> boundedContextComponentDoc() {
        return new Attribute<BoundedContextComponentDoc>() {
            @Override
            public BoundedContextComponentDoc value() {
                return componentDoc.adapt();
            }

            @Override
            public void value(BoundedContextComponentDoc value) {
                componentDoc = BoundedContextComponentDocData.adapt(value);
            }
        };
    }

    private BoundedContextComponentDocData componentDoc;
}
