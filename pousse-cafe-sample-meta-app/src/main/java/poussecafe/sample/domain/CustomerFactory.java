package poussecafe.sample.domain;

import poussecafe.domain.Factory;

public class CustomerFactory extends Factory<CustomerKey, Customer, Customer.Attributes> {

    public Customer createCustomer(CustomerKey customerKey) {
        return newAggregateWithKey(customerKey);
    }

}
