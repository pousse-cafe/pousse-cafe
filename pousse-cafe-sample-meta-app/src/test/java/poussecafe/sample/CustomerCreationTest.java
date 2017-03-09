package poussecafe.sample;

import org.junit.Test;
import poussecafe.sample.command.CreateCustomer;
import poussecafe.sample.configuration.CustomerConfiguration;
import poussecafe.sample.domain.Customer;
import poussecafe.sample.domain.CustomerKey;
import poussecafe.sample.workflow.CustomerCreation;
import poussecafe.test.MetaApplicationTest;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class CustomerCreationTest extends MetaApplicationTest {

    private CustomerKey customerKey;

    @Override
    protected void registerComponents() {
        configuration.registerAggregate(new CustomerConfiguration());
        configuration.registerWorkflow(new CustomerCreation());
    }

    @Test
    public void customerCreation() {
        givenCustomerKey();
        whenSubmittingCommand();
        thenCustomerIsCreated();
    }

    private void givenCustomerKey() {
        customerKey = new CustomerKey("customer-id");
    }

    private void whenSubmittingCommand() {
        processAndAssertSuccess(new CreateCustomer(customerKey));
    }

    private void thenCustomerIsCreated() {
        assertThat(find(Customer.class, customerKey), notNullValue());
    }
}
