package poussecafe.domain;

import poussecafe.property.Property;

public class SimpleAggregateData implements SimpleAggregate.Data {

    @Override
    public Property<SimpleAggregateKey> key() {
        return new Property<SimpleAggregateKey>() {
            @Override
            public SimpleAggregateKey get() {
                return new SimpleAggregateKey(key);
            }

            @Override
            public void set(SimpleAggregateKey value) {
                key = value.getId();
            }
        };
    }

    private String key;
}
