package poussecafe.sample.domain;

import poussecafe.domain.AggregateData;
import poussecafe.domain.AggregateRoot;

public class Customer extends AggregateRoot<CustomerKey, Customer.Data> {

    public static interface Data extends AggregateData<CustomerKey> {

    }
}
