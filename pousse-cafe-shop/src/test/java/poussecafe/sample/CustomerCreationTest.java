package poussecafe.sample;

import java.util.List;
import org.junit.Test;
import poussecafe.environment.BoundedContext;
import poussecafe.shop.Shop;
import poussecafe.shop.command.CreateCustomer;
import poussecafe.shop.domain.Customer;
import poussecafe.shop.domain.CustomerKey;
import poussecafe.shop.process.CustomerCreation;
import poussecafe.test.PousseCafeTest;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class CustomerCreationTest extends PousseCafeTest {

    private CustomerKey customerKey;

    @Override
    protected List<BoundedContext> boundedContexts() {
        return asList(Shop.configure().defineAndImplementDefault().build());
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
        customerCreation.createCustomer(new CreateCustomer(customerKey));
    }

    private CustomerCreation customerCreation;

    private void thenCustomerIsCreated() {
        assertThat(find(Customer.class, customerKey), notNullValue());
    }
}
