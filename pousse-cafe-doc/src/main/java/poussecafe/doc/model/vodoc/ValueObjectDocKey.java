package poussecafe.doc.model.vodoc;

import poussecafe.domain.ValueObject;
import poussecafe.util.StringKey;

public class ValueObjectDocKey extends StringKey implements ValueObject {

    public static ValueObjectDocKey ofClassName(String className) {
        return new ValueObjectDocKey(className);
    }

    private ValueObjectDocKey(String className) {
        super(className);
    }
}
