package poussecafe.doc.model.factorydoc;

import poussecafe.domain.ValueObject;
import poussecafe.util.StringId;

/**
 * @trivial
 */
public class FactoryDocId extends StringId implements ValueObject {

    public static FactoryDocId ofClassName(String className) {
        return new FactoryDocId(className);
    }

    private FactoryDocId(String className) {
        super(className);
    }
}
