package poussecafe.doc.model.servicedoc;

import java.io.Serializable;
import poussecafe.attribute.Attribute;
import poussecafe.doc.model.BoundedContextComponentDoc;
import poussecafe.doc.model.BoundedContextComponentDocData;

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
                return boundedContextComponentDoc.adapt();
            }

            @Override
            public void value(BoundedContextComponentDoc value) {
                boundedContextComponentDoc = BoundedContextComponentDocData.adapt(value);
            }
        };
    }

    private BoundedContextComponentDocData boundedContextComponentDoc;
}
