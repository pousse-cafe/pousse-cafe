package poussecafe.sample.domain;

import poussecafe.domain.AggregateRoot;
import poussecafe.storable.IdentifiedStorableData;

public class Customer extends AggregateRoot<CustomerKey, Customer.Data> {

    public static interface Data extends IdentifiedStorableData<CustomerKey> {

    }

}
