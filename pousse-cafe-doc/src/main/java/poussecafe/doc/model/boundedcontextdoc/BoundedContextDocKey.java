package poussecafe.doc.model.boundedcontextdoc;

import poussecafe.domain.ValueObject;
import poussecafe.util.StringKey;

public class BoundedContextDocKey extends StringKey implements ValueObject {

    public static BoundedContextDocKey ofPackageName(String packageName) {
        return new BoundedContextDocKey(packageName);
    }

    public BoundedContextDocKey(String value) {
        super(value);
    }

}
