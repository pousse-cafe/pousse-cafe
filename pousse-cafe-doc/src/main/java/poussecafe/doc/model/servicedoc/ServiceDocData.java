package poussecafe.doc.model.servicedoc;

import java.io.Serializable;
import poussecafe.storable.Property;

@SuppressWarnings("serial")
public class ServiceDocData implements ServiceDoc.Data, Serializable {

    @Override
    public Property<ServiceDocKey> key() {
        return new Property<ServiceDocKey>() {
            @Override
            public ServiceDocKey get() {
                return new ServiceDocKey(boundedContextKey, name);
            }

            @Override
            public void set(ServiceDocKey value) {
                boundedContextKey = value.boundedContextKey();
                name = value.name();
            }
        };
    }

    private String boundedContextKey;

    private String name;

    @Override
    public Property<String> description() {
        return new Property<String>() {
            @Override
            public String get() {
                return description;
            }

            @Override
            public void set(String value) {
                description = value;
            }
        };
    }

    private String description;
}
