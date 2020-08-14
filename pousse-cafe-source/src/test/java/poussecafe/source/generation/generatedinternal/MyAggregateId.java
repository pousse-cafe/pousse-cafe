package poussecafe.source.generation.generatedinternal;

import poussecafe.domain.ValueObject;
import poussecafe.util.StringId;

public class MyAggregateId extends StringId implements ValueObject {

    public MyAggregateId(String value) {
        super(value);
    }
}