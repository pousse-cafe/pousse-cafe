package poussecafe.sample.domain;

import poussecafe.domain.Factory;

public class CustomerFactory extends Factory<CustomerKey, Customer, Customer.Data> {

    @Override
    protected Customer newAggregate() {
        return new Customer();
    }

    public Customer createCustomer(CustomerKey customerKey) {
        return newAggregateWithKey(customerKey);
    }

}
