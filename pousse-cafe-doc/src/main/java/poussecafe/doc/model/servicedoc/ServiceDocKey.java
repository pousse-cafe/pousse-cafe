package poussecafe.doc.model.servicedoc;

import poussecafe.domain.ValueObject;
import poussecafe.util.StringKey;

public class ServiceDocKey extends StringKey implements ValueObject {

    public static ServiceDocKey ofClassName(String className) {
        return new ServiceDocKey(className);
    }

    private ServiceDocKey(String className) {
        super(className);
    }
}
