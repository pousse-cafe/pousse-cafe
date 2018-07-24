package poussecafe.doc.model.domainprocessdoc;

import poussecafe.domain.ValueObject;
import poussecafe.util.StringKey;

public class DomainProcessDocKey extends StringKey implements ValueObject {

    public static DomainProcessDocKey ofClassName(String className) {
        return new DomainProcessDocKey(className);
    }

    private DomainProcessDocKey(String className) {
        super(className);
    }
}
