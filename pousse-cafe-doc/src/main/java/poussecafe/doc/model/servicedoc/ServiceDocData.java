package poussecafe.doc.model.servicedoc;

import java.io.Serializable;
import poussecafe.attribute.Attribute;
import poussecafe.doc.model.BoundedContextComponentDoc;
import poussecafe.doc.model.BoundedContextComponentDocData;

@SuppressWarnings("serial")
public class ServiceDocData implements ServiceDoc.Attributes, Serializable {

    @Override
    public Attribute<ServiceDocKey> key() {
        return new Attribute<ServiceDocKey>() {
            @Override
            public ServiceDocKey value() {
                return ServiceDocKey.ofClassName(className);
            }

            @Override
            public void value(ServiceDocKey value) {
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
                return boundedContextComponentDoc.toModel();
            }

            @Override
            public void value(BoundedContextComponentDoc value) {
                boundedContextComponentDoc = BoundedContextComponentDocData.of(value);
            }
        };
    }

    private BoundedContextComponentDocData boundedContextComponentDoc;
}
