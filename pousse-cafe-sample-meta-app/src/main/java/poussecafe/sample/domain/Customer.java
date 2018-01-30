package poussecafe.sample.domain;

import poussecafe.domain.AggregateRoot;
import poussecafe.storable.Property;
import poussecafe.storable.StorableData;

public class Customer extends AggregateRoot<CustomerKey, Customer.Data> {

    @Override
    public CustomerKey getKey() {
        return new CustomerKey(getData().key().get());
    }

    @Override
    public void setKey(CustomerKey key) {
        getData().key().set(key.getValue());
    }

    public static interface Data extends StorableData {

        Property<String> key();
    }

}
