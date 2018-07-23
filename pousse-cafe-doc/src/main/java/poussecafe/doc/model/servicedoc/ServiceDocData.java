package poussecafe.doc.model.servicedoc;

import java.io.Serializable;
import poussecafe.doc.model.BoundedContextComponentDoc;
import poussecafe.doc.model.BoundedContextComponentDocData;
import poussecafe.storable.Property;

@SuppressWarnings("serial")
public class ServiceDocData implements ServiceDoc.Data, Serializable {

    @Override
    public Property<ServiceDocKey> key() {
        return new Property<ServiceDocKey>() {
            @Override
            public ServiceDocKey get() {
                return ServiceDocKey.ofClassName(className);
            }

            @Override
            public void set(ServiceDocKey value) {
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
                return boundedContextComponentDoc.toModel();
            }

            @Override
            public void set(BoundedContextComponentDoc value) {
                boundedContextComponentDoc = BoundedContextComponentDocData.of(value);
            }
        };
    }

    private BoundedContextComponentDocData boundedContextComponentDoc;
}
