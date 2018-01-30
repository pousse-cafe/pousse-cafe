package poussecafe.sample;

import org.junit.Test;
import poussecafe.sample.command.CreateCustomer;
import poussecafe.sample.domain.Customer;
import poussecafe.sample.domain.CustomerFactory;
import poussecafe.sample.domain.CustomerKey;
import poussecafe.sample.domain.CustomerRepository;
import poussecafe.sample.workflow.CustomerCreation;
import poussecafe.storable.StorableDefinition;
import poussecafe.test.MetaApplicationTest;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class CustomerCreationTest extends MetaApplicationTest {

    private CustomerKey customerKey;

    @Override
    protected void registerComponents() {
        context().environment().defineStorable(new StorableDefinition.Builder()
                .withStorableClass(Customer.class)
                .withFactoryClass(CustomerFactory.class)
                .withRepositoryClass(CustomerRepository.class)
                .build());

        context().environment().defineProcess(CustomerCreation.class);
    }

    @Test
    public void customerCreation() {
        givenCustomerKey();
        whenCreatingCustomer();
        thenCustomerIsCreated();
    }

    private void givenCustomerKey() {
        customerKey = new CustomerKey("customer-id");
    }

    private void whenCreatingCustomer() {
        context().getProcess(CustomerCreation.class).createCustomer(new CreateCustomer(customerKey));
    }

    private void thenCustomerIsCreated() {
        assertThat(find(Customer.class, customerKey), notNullValue());
    }
}
