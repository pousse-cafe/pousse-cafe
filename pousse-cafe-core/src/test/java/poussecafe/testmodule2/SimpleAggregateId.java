package poussecafe.testmodule2;

import java.util.UUID;
import poussecafe.util.StringId;

public class SimpleAggregateId extends StringId {

    public SimpleAggregateId() {
        super(UUID.randomUUID().toString());
    }

    public SimpleAggregateId(String id) {
        super(id);
    }

    public String getId() {
        return stringValue();
    }
}
