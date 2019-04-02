package poussecafe.doc.model.boundedcontextdoc;

import poussecafe.domain.ValueObject;
import poussecafe.util.StringId;

public class BoundedContextDocId extends StringId implements ValueObject {

    public static BoundedContextDocId ofPackageName(String packageName) {
        return new BoundedContextDocId(packageName);
    }

    public BoundedContextDocId(String value) {
        super(value);
    }

}
