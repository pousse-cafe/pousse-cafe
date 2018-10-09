package poussecafe.sample.domain;

import poussecafe.domain.AggregateRoot;
import poussecafe.domain.EntityData;

public class Customer extends AggregateRoot<CustomerKey, Customer.Data> {

    public static interface Data extends EntityData<CustomerKey> {

    }

}
