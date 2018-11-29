package poussecafe.sample;

import java.util.List;
import org.junit.Test;
import poussecafe.context.BoundedContext;
import poussecafe.sample.command.CreateCustomer;
import poussecafe.sample.domain.Customer;
import poussecafe.sample.domain.CustomerKey;
import poussecafe.sample.process.CustomerCreation;
import poussecafe.test.MetaApplicationTest;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class CustomerCreationTest extends MetaApplicationTest {

    private CustomerKey customerKey;

    @Override
    protected List<BoundedContext> testBundle() {
        return asList(new SampleMetaAppBoundedContextDefinition().withDefaultImplementation().build());
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
        context().getDomainProcess(CustomerCreation.class).createCustomer(new CreateCustomer(customerKey));
    }

    private void thenCustomerIsCreated() {
        assertThat(find(Customer.class, customerKey), notNullValue());
    }
}
