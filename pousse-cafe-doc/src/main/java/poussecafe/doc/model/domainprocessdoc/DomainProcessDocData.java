package poussecafe.doc.model.domainprocessdoc;

import java.io.Serializable;
import poussecafe.attribute.Attribute;
import poussecafe.doc.model.BoundedContextComponentDoc;
import poussecafe.doc.model.BoundedContextComponentDocData;

@SuppressWarnings("serial")
public class DomainProcessDocData implements DomainProcessDoc.Attributes, Serializable {

    @Override
    public Attribute<DomainProcessDocKey> key() {
        return new Attribute<DomainProcessDocKey>() {
            @Override
            public DomainProcessDocKey value() {
                return new DomainProcessDocKey(className);
            }

            @Override
            public void value(DomainProcessDocKey value) {
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
