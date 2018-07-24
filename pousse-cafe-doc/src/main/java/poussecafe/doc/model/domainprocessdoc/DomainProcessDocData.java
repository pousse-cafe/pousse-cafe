package poussecafe.doc.model.domainprocessdoc;

import java.io.Serializable;
import poussecafe.doc.model.BoundedContextComponentDoc;
import poussecafe.doc.model.BoundedContextComponentDocData;
import poussecafe.storable.Property;

@SuppressWarnings("serial")
public class DomainProcessDocData implements DomainProcessDoc.Data, Serializable {

    @Override
    public Property<DomainProcessDocKey> key() {
        return new Property<DomainProcessDocKey>() {
            @Override
            public DomainProcessDocKey get() {
                return DomainProcessDocKey.ofClassName(className);
            }

            @Override
            public void set(DomainProcessDocKey value) {
                className = value.getValue();
            }
        };
    }

    private String className;

    @Override
    public Property<BoundedContextComponentDoc> boundedContextComponentDoc() {
        return new Property<BoundedContextComponentDoc>() {
            @Override
            public BoundedContextComponentDoc get() {
                return componentDoc.toModel();
            }

            @Override
            public void set(BoundedContextComponentDoc value) {
                componentDoc = BoundedContextComponentDocData.of(value);
            }
        };
    }

    private BoundedContextComponentDocData componentDoc;
}
