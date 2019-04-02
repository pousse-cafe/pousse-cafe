package poussecafe.doc.model.entitydoc;

import poussecafe.domain.ValueObject;
import poussecafe.util.StringId;

public class EntityDocId extends StringId implements ValueObject {

    public static EntityDocId ofClassName(String className) {
        return new EntityDocId(className);
    }

    private EntityDocId(String className) {
        super(className);
    }
}
