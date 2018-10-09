package poussecafe.sample;

import java.util.List;
import org.junit.Test;
import poussecafe.context.BoundedContext;
import poussecafe.sample.command.PlaceOrder;
import poussecafe.sample.domain.CustomerKey;
import poussecafe.sample.domain.Order;
import poussecafe.sample.domain.OrderDescription;
import poussecafe.sample.domain.OrderKey;
import poussecafe.sample.domain.ProductKey;
import poussecafe.sample.process.OrderPlacement;
import poussecafe.test.MetaApplicationTest;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class OrderManagementTest extends MetaApplicationTest {

    private CustomerKey customerKey;

    private ProductKey productKey;

    private OrderDescription description;

    @Override
    protected List<BoundedContext> testBundle() {
        return asList(new SampleMetaAppBoundedContext());
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
        context().getDomainProcess(OrderPlacement.class).placeOrder(new PlaceOrder(productKey, description));
    }

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
