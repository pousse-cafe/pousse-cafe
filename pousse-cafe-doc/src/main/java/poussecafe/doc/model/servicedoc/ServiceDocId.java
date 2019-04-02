package poussecafe.doc.model.servicedoc;

import poussecafe.domain.ValueObject;
import poussecafe.util.StringId;

public class ServiceDocId extends StringId implements ValueObject {

    public static ServiceDocId ofClassName(String className) {
        return new ServiceDocId(className);
    }

    private ServiceDocId(String className) {
        super(className);
    }
}
