package poussecafe.shop.process;

import poussecafe.process.DomainProcess;
import poussecafe.shop.command.CreateCustomer;
import poussecafe.shop.domain.Customer;
import poussecafe.shop.domain.CustomerFactory;
import poussecafe.shop.domain.CustomerRepository;

public class CustomerCreation extends DomainProcess {

    private CustomerFactory factory;

    private CustomerRepository repository;

    public void createCustomer(CreateCustomer command) {
        Customer customer = factory.createCustomer(command.getCustomerKey());
        runInTransaction(Customer.class, () -> repository.add(customer));
    }
}
