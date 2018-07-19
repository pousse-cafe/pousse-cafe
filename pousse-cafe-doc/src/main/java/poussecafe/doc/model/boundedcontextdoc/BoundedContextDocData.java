package poussecafe.doc.model.boundedcontextdoc;

import java.io.Serializable;
import poussecafe.storable.Property;

@SuppressWarnings("serial")
public class BoundedContextDocData implements BoundedContextDoc.Data, Serializable {

    @Override
    public Property<String> key() {
        return new Property<String>() {
            @Override
            public String get() {
                return key;
            }

            @Override
            public void set(String value) {
                key = value;
            }
        };
    }

    private String key;

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

    @Override
    public Property<String> packageName() {
        return new Property<String>() {
            @Override
            public String get() {
                return packageName;
            }

            @Override
            public void set(String value) {
                packageName = value;
            }
        };
    }

    private String packageName;
}
