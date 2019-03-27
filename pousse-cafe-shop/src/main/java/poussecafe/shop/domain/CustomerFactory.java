package poussecafe.shop.domain;

import poussecafe.discovery.MessageListener;
import poussecafe.domain.Factory;
import poussecafe.shop.command.CreateCustomer;

public class CustomerFactory extends Factory<CustomerKey, Customer, Customer.Attributes> {

    /**
     * @process CustomerCreation
     */
    @MessageListener
    public Customer createCustomer(CreateCustomer command) {
        return newAggregateWithKey(command.customerKey().value());
    }

}
