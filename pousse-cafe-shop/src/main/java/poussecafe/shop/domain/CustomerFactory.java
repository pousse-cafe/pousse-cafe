package poussecafe.shop.domain;

import poussecafe.discovery.MessageListener;
import poussecafe.domain.Factory;
import poussecafe.shop.command.CreateCustomer;

public class CustomerFactory extends Factory<CustomerId, Customer, Customer.Attributes> {

    /**
     * @process CustomerCreation
     */
    @MessageListener
    public Customer createCustomer(CreateCustomer command) {
        return newAggregateWithId(command.customerId().value());
    }

}
