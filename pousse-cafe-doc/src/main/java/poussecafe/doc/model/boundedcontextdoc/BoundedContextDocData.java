package poussecafe.doc.model.boundedcontextdoc;

import java.io.Serializable;
import poussecafe.doc.model.ComponentDoc;
import poussecafe.doc.model.ComponentDocData;
import poussecafe.storable.Property;

@SuppressWarnings("serial")
public class BoundedContextDocData implements BoundedContextDoc.Data, Serializable {

    @Override
    public Property<BoundedContextDocKey> key() {
        return new Property<BoundedContextDocKey>() {
            @Override
            public BoundedContextDocKey get() {
                return BoundedContextDocKey.ofClassName(className);
            }

            @Override
            public void set(BoundedContextDocKey value) {
                className = value.getValue();
            }
        };
    }

    private String className;

    @Override
    public Property<ComponentDoc> componentDoc() {
        return new Property<ComponentDoc>() {
            @Override
            public ComponentDoc get() {
                return componentDoc.toModel();
            }

            @Override
            public void set(ComponentDoc value) {
                componentDoc = ComponentDocData.of(value);
            }
        };
    }

    private ComponentDocData componentDoc;

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
