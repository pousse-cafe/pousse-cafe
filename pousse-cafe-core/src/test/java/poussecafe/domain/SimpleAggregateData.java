package poussecafe.domain;

import poussecafe.inmemory.InlineProperty;
import poussecafe.storable.Property;

public class SimpleAggregateData implements SimpleAggregate.Data {

    @Override
    public Property<String> id() {
        return id;
    }

    private InlineProperty<String> id;
}
