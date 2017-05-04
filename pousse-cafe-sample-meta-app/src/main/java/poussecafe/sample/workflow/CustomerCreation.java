package poussecafe.sample.workflow;

import poussecafe.messaging.CommandListener;
import poussecafe.sample.command.CreateCustomer;
import poussecafe.sample.domain.Customer;
import poussecafe.sample.domain.CustomerFactory;
import poussecafe.sample.domain.CustomerRepository;
import poussecafe.service.Workflow;

public class CustomerCreation extends Workflow {

    private CustomerFactory factory;

    private CustomerRepository repository;

    @CommandListener
    public void createCustomer(CreateCustomer command) {
        Customer customer = factory.createCustomer(command.getCustomerKey());
        runInTransaction(Customer.Data.class, () -> repository.add(customer));
    }

    public void setFactory(CustomerFactory factory) {
        this.factory = factory;
    }

    public void setRepository(CustomerRepository repository) {
        this.repository = repository;
    }
}
