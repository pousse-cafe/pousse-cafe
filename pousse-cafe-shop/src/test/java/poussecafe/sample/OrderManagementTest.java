package poussecafe.sample;

import java.util.List;
import org.junit.Test;
import poussecafe.environment.BoundedContext;
import poussecafe.shop.Shop;
import poussecafe.shop.command.PlaceOrder;
import poussecafe.shop.domain.CustomerKey;
import poussecafe.shop.domain.Order;
import poussecafe.shop.domain.OrderDescription;
import poussecafe.shop.domain.OrderKey;
import poussecafe.shop.domain.ProductKey;
import poussecafe.shop.process.OrderPlacement;
import poussecafe.test.PousseCafeTest;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class OrderManagementTest extends PousseCafeTest {

    private CustomerKey customerKey;

    private ProductKey productKey;

    private OrderDescription description;

    @Override
    protected List<BoundedContext> boundedContexts() {
        return asList(Shop.configure().defineAndImplementDefault().build());
    }

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
        description = new OrderDescription();
        description.customerKey = customerKey;
        description.reference = "ref";
        description.units = 1;
        orderPlacement.placeOrder(new PlaceOrder(productKey, description));
    }

    private OrderPlacement orderPlacement;

    private void thenOrderCreated() {
        assertThat(find(Order.class, orderKey()), notNullValue());
    }

    private OrderKey orderKey() {
        return new OrderKey(productKey, description.customerKey, description.reference);
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
