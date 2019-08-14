package poussecafe.doc.model.aggregatedoc;

import poussecafe.domain.ValueObject;
import poussecafe.util.StringId;

/**
 * @trivial
 */
public class AggregateDocId extends StringId implements ValueObject {

    public static AggregateDocId ofClassName(String className) {
        return new AggregateDocId(className);
    }

    private AggregateDocId(String id) {
        super(id);
    }
}
