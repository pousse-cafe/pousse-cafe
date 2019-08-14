package poussecafe.doc.model.domainprocessdoc;

import poussecafe.domain.ValueObject;
import poussecafe.util.StringId;

/**
 * @trivial
 */
public class DomainProcessDocId extends StringId implements ValueObject {

    public DomainProcessDocId(String className) {
        super(className);
    }
}
