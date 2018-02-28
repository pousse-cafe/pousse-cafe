package poussecafe.domain;

import poussecafe.storable.Property;
import poussecafe.storage.memory.InMemoryActiveData;

@SuppressWarnings("serial")
public class SimpleAggregateData extends InMemoryActiveData<SimpleAggregateKey> implements SimpleAggregate.Data {

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
