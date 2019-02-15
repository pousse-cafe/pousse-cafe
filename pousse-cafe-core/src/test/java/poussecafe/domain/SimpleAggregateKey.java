package poussecafe.domain;

import java.util.UUID;
import poussecafe.util.StringKey;

public class SimpleAggregateKey extends StringKey {

    public SimpleAggregateKey() {
        super(UUID.randomUUID().toString());
    }

    public SimpleAggregateKey(String id) {
        super(id);
    }

    public String getId() {
        return getValue();
    }
}
