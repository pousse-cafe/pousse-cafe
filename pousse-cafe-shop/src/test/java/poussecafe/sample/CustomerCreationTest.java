package poussecafe.sample;

import org.junit.Test;
import poussecafe.shop.command.CreateCustomer;
import poussecafe.shop.domain.Customer;
import poussecafe.shop.domain.CustomerId;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class CustomerCreationTest extends ShopTest {

    private CustomerId customerId;

    @Test
    public void customerCreation() {
        givenCustomerId();
        whenCreatingCustomer();
        thenCustomerIsCreated();
    }

    private void givenCustomerId() {
        customerId = new CustomerId("customer-id");
    }

    private void whenCreatingCustomer() {
        CreateCustomer command = newCommand(CreateCustomer.class);
        command.customerId().value(customerId);
        submitCommand(command);
    }

    private void thenCustomerIsCreated() {
        assertThat(find(Customer.class, customerId), notNullValue());
    }
}
