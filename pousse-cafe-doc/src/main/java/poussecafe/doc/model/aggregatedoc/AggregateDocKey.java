package poussecafe.doc.model.aggregatedoc;

import poussecafe.domain.ValueObject;
import poussecafe.util.StringKey;

public class AggregateDocKey extends StringKey implements ValueObject {

    public static AggregateDocKey ofClassName(String className) {
        return new AggregateDocKey(className);
    }

    private AggregateDocKey(String id) {
        super(id);
    }
}
