package poussecafe.doc.model.entitydoc;

import poussecafe.domain.ValueObject;
import poussecafe.util.StringKey;

public class EntityDocKey extends StringKey implements ValueObject {

    public static EntityDocKey ofClassName(String className) {
        return new EntityDocKey(className);
    }

    private EntityDocKey(String className) {
        super(className);
    }
}
