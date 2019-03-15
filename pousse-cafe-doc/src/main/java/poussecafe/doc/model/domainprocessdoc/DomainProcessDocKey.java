package poussecafe.doc.model.domainprocessdoc;

import poussecafe.domain.ValueObject;
import poussecafe.util.StringKey;

public class DomainProcessDocKey extends StringKey implements ValueObject {

    public DomainProcessDocKey(String className) {
        super(className);
    }
}
