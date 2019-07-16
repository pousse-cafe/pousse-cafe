package poussecafe.sample;

import org.junit.Test;
import poussecafe.shop.command.CreateCustomer;
import poussecafe.shop.domain.Customer;
import poussecafe.shop.domain.CustomerId;

import static org.junit.Assert.assertTrue;

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
        assertTrue(getOptional(Customer.class, customerId).isPresent());
    }
}
