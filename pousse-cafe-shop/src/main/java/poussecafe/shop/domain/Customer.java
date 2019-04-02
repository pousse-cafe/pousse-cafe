package poussecafe.shop.domain;

import poussecafe.discovery.Aggregate;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.EntityAttributes;

@Aggregate(
    factory = CustomerFactory.class,
    repository = CustomerRepository.class
)
public class Customer extends AggregateRoot<CustomerId, Customer.Attributes> {

    public static interface Attributes extends EntityAttributes<CustomerId> {

    }

}
