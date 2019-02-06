package poussecafe.sample.domain;

import poussecafe.contextconfigurer.Aggregate;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.EntityAttributes;

@Aggregate(
    factory = CustomerFactory.class,
    repository = CustomerRepository.class
)
public class Customer extends AggregateRoot<CustomerKey, Customer.Attributes> {

    public static interface Attributes extends EntityAttributes<CustomerKey> {

    }

}
