package poussecafe.doc.model.vodoc;

import poussecafe.domain.ValueObject;
import poussecafe.util.StringId;

public class ValueObjectDocId extends StringId implements ValueObject {

    public static ValueObjectDocId ofClassName(String className) {
        return new ValueObjectDocId(className);
    }

    private ValueObjectDocId(String className) {
        super(className);
    }
}
