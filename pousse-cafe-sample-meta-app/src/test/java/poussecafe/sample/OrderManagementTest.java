package poussecafe.sample;

import org.junit.Test;
import poussecafe.sample.command.PlaceOrder;
import poussecafe.sample.configuration.OrderConfiguration;
import poussecafe.sample.configuration.ProductConfiguration;
import poussecafe.sample.domain.CustomerKey;
import poussecafe.sample.domain.Order;
import poussecafe.sample.domain.OrderDescription;
import poussecafe.sample.domain.OrderKey;
import poussecafe.sample.domain.Product;
import poussecafe.sample.domain.ProductKey;
import poussecafe.sample.workflow.OrderManagement;
import poussecafe.test.MetaApplicationTest;
import poussecafe.test.TestConfigurationBuilder;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class OrderManagementTest extends MetaApplicationTest {

    private CustomerKey customerKey;

    private ProductKey productKey;

    private OrderDescription description;

    @Override
    protected void registerComponents() {
        configuration.registerAggregate(new TestConfigurationBuilder()
                .withConfiguration(new ProductConfiguration())
                .withData(Product.Data.class)
                .build());

        configuration.registerAggregate(new TestConfigurationBuilder()
                .withConfiguration(new OrderConfiguration())
                .withData(Order.Data.class)
                .build());

        configuration.registerWorkflow(new OrderManagement());
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
        processAndAssertSuccess(new PlaceOrder(productKey, description));
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
