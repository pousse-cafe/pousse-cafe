package poussecafe.sample;

import org.junit.Test;
import poussecafe.shop.command.CreateCustomer;
import poussecafe.shop.domain.Customer;
import poussecafe.shop.domain.CustomerKey;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class CustomerCreationTest extends ShopTest {

    private CustomerKey customerKey;

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
        CreateCustomer command = newCommand(CreateCustomer.class);
        command.customerKey().value(customerKey);
        submitCommand(command);
    }

    private void thenCustomerIsCreated() {
        assertThat(find(Customer.class, customerKey), notNullValue());
    }
}
