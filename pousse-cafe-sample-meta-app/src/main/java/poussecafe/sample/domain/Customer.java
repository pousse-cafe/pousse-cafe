package poussecafe.sample.domain;

import poussecafe.contextconfigurer.Aggregate;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.EntityData;

@Aggregate(
    factory = CustomerFactory.class,
    repository = CustomerRepository.class
)
public class Customer extends AggregateRoot<CustomerKey, Customer.Data> {

    public static interface Data extends EntityData<CustomerKey> {

    }

}
