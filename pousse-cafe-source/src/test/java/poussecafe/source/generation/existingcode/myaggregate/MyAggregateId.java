package poussecafe.source.generation.existingcode.myaggregate;

import poussecafe.domain.ValueObject;
import poussecafe.util.StringId;

public class MyAggregateId extends StringId implements ValueObject {

    public MyAggregateId(String value) {
        super(value);
    }
}
