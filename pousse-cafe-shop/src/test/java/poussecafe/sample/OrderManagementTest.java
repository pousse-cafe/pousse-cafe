package poussecafe.sample;

import org.junit.Test;
import poussecafe.shop.command.PlaceOrder;
import poussecafe.shop.domain.CustomerKey;
import poussecafe.shop.domain.Order;
import poussecafe.shop.domain.OrderDescription;
import poussecafe.shop.domain.OrderKey;
import poussecafe.shop.domain.ProductKey;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class OrderManagementTest extends ShopTest {

    private CustomerKey customerKey;

    private ProductKey productKey;

    private OrderDescription description;

    @Test
    public void placingOrderCreatesOrder() {
        givenCustomer();
        givenProductWithUnits(true);
        whenPlacingOrder();
        thenOrderCreated();
    }

    private void givenCustomer() {
        customerKey = new CustomerKey("customer-id");
    }

    private void givenProductWithUnits(boolean withUnits) {
        productKey = new ProductKey("product-1");
        if (withUnits) {
            loadDataFile("/data/placingOrderProductWithUnits.json");
        } else {
            loadDataFile("/data/placingOrderProductWithoutUnits.json");
        }
    }

    private void whenPlacingOrder() {
        PlaceOrder command = newCommand(PlaceOrder.class);
        command.productKey().value(productKey);
        description = new OrderDescription.Builder()
                .customerKey(customerKey)
                .reference("ref")
                .units(1)
                .build();
        command.description().value(description);
        submitCommand(command);
    }

    private void thenOrderCreated() {
        assertThat(find(Order.class, orderKey()), notNullValue());
    }

    private OrderKey orderKey() {
        return new OrderKey(productKey, description.customerKey(), description.reference());
    }

    @Test
    public void placingOrderWithNotEnoughUnitsDoesNotCreatesOrder() {
        givenCustomer();
        givenProductWithUnits(false);
        whenPlacingOrder();
        thenNoOrderCreated();
    }

    private void thenNoOrderCreated() {
        assertThat(find(Order.class, orderKey()), nullValue());
    }
}
