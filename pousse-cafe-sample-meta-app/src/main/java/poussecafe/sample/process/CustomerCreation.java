package poussecafe.sample.process;

import poussecafe.sample.command.CreateCustomer;
import poussecafe.sample.domain.Customer;
import poussecafe.sample.domain.CustomerFactory;
import poussecafe.sample.domain.CustomerRepository;
import poussecafe.service.DomainProcess;

public class CustomerCreation extends DomainProcess {

    private CustomerFactory factory;

    private CustomerRepository repository;

    public void createCustomer(CreateCustomer command) {
        Customer customer = factory.createCustomer(command.getCustomerKey());
        runInTransaction(Customer.class, () -> repository.add(customer));
    }
}
