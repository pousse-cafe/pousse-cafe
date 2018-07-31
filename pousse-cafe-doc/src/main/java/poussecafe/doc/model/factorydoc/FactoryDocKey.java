package poussecafe.doc.model.factorydoc;

import poussecafe.domain.ValueObject;
import poussecafe.util.StringKey;

public class FactoryDocKey extends StringKey implements ValueObject {

    public static FactoryDocKey ofClassName(String className) {
        return new FactoryDocKey(className);
    }

    private FactoryDocKey(String className) {
        super(className);
    }
}
