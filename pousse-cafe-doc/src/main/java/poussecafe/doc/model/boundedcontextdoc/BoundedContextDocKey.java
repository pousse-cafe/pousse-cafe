package poussecafe.doc.model.boundedcontextdoc;

import poussecafe.domain.ValueObject;
import poussecafe.util.StringKey;

public class BoundedContextDocKey extends StringKey implements ValueObject {

    public static BoundedContextDocKey ofClassName(String className) {
        return new BoundedContextDocKey(className);
    }

    private BoundedContextDocKey(String value) {
        super(value);
    }

}
