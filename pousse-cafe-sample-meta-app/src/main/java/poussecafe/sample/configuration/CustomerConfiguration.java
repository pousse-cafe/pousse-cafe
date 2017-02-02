package poussecafe.sample.configuration;

import poussecafe.configuration.AggregateConfiguration;
import poussecafe.sample.domain.Customer;
import poussecafe.sample.domain.CustomerFactory;
import poussecafe.sample.domain.CustomerKey;
import poussecafe.sample.domain.CustomerRepository;

public class CustomerConfiguration
extends AggregateConfiguration<CustomerKey, Customer, Customer.Data, CustomerFactory, CustomerRepository> {

    public CustomerConfiguration() {
        super(Customer.class, CustomerFactory.class, CustomerRepository.class);
    }

}
