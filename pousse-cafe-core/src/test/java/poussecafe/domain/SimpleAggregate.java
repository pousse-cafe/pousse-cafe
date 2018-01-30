package poussecafe.domain;

import poussecafe.storable.Property;
import poussecafe.storable.StorableData;

public class SimpleAggregate extends AggregateRoot<SimpleAggregateKey, SimpleAggregate.Data> {

    @Override
    public SimpleAggregateKey getKey() {
        return new SimpleAggregateKey(getData().id().get());
    }

    @Override
    public void setKey(SimpleAggregateKey key) {
        getData().id().set(key.getId());
    }

    public static interface Data extends StorableData {

        Property<String> id();
    }

}
