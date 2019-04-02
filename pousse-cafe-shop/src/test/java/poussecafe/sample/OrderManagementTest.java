package poussecafe.sample;

import org.junit.Test;
import poussecafe.shop.command.PlaceOrder;
import poussecafe.shop.domain.CustomerId;
import poussecafe.shop.domain.Order;
import poussecafe.shop.domain.OrderDescription;
import poussecafe.shop.domain.OrderId;
import poussecafe.shop.domain.ProductId;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class OrderManagementTest extends ShopTest {

    private CustomerId customerId;

    private ProductId productId;

    private OrderDescription description;

    @Test
    public void placingOrderCreatesOrder() {
        givenCustomer();
        givenProductWithUnits(true);
        whenPlacingOrder();
        thenOrderCreated();
    }

    private void givenCustomer() {
        customerId = new CustomerId("customer-id");
    }

    private void givenProductWithUnits(boolean withUnits) {
        productId = new ProductId("product-1");
        if (withUnits) {
            loadDataFile("/data/placingOrderProductWithUnits.json");
        } else {
            loadDataFile("/data/placingOrderProductWithoutUnits.json");
        }
    }

    private void whenPlacingOrder() {
        PlaceOrder command = newCommand(PlaceOrder.class);
        command.productId().value(productId);
        description = new OrderDescription.Builder()
                .customerId(customerId)
                .reference("ref")
                .units(1)
                .build();
        command.description().value(description);
        submitCommand(command);
    }

    private void thenOrderCreated() {
        assertThat(find(Order.class, orderId()), notNullValue());
    }

    private OrderId orderId() {
        return new OrderId(productId, description.customerId(), description.reference());
    }

    @Test
    public void placingOrderWithNotEnoughUnitsDoesNotCreatesOrder() {
        givenCustomer();
        givenProductWithUnits(false);
        whenPlacingOrder();
        thenNoOrderCreated();
    }

    private void thenNoOrderCreated() {
        assertThat(find(Order.class, orderId()), nullValue());
    }
}
