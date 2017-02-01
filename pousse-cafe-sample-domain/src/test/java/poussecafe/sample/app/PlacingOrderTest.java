package poussecafe.sample.app;

import org.junit.Test;
import poussecafe.sample.domain.Order;
import poussecafe.sample.domain.Order.OrderData;
import poussecafe.sample.domain.OrderDescription;
import poussecafe.sample.domain.OrderFactory;
import poussecafe.sample.domain.OrderKey;
import poussecafe.sample.domain.OrderRepository;
import poussecafe.sample.domain.Product;
import poussecafe.sample.domain.Product.ProductData;
import poussecafe.test.TestConfigurationBuilder;
import poussecafe.test.WorkflowTest;
import poussecafe.sample.domain.ProductFactory;
import poussecafe.sample.domain.ProductKey;
import poussecafe.sample.domain.ProductRepository;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class PlacingOrderTest extends WorkflowTest {

    private PlacingOrder placingOrder;

    private ProductKey productKey;

    private OrderDescription description;

    @Override
    protected void registerActors() {
        configuration.registerAggregateConfiguration(new TestConfigurationBuilder()
                .withStorable(Product.class)
                .withFactory(ProductFactory.class)
                .withRepository(ProductRepository.class)
                .withData(ProductData.class)
                .build());

        configuration.registerAggregateConfiguration(new TestConfigurationBuilder()
                .withStorable(Order.class)
                .withFactory(OrderFactory.class)
                .withRepository(OrderRepository.class)
                .withData(OrderData.class)
                .build());

        placingOrder = new PlacingOrder();
        configuration.registerWorkflow(placingOrder);
    }

    @Test
    public void placingOrderCreatesOrder() {
        givenContext();
        givenAvailableProduct();
        whenPlacingOrder();
        thenOrderCreated();
    }

    private void whenPlacingOrder() {
        description = new OrderDescription();
        description.reference = "ref";
        description.units = 1;
        processAndAssertSuccess(new PlaceOrder(productKey, description));
    }

    private void givenAvailableProduct() {
        productKey = new ProductKey("product-1");
        processAndAssertSuccess(new CreateProduct(productKey));
        processAndAssertSuccess(new AddUnits(productKey, 10));
    }

    private void thenOrderCreated() {
        OrderKey orderKey = new OrderKey(productKey, description.reference);
        Order order = getEventually(Order.class, orderKey);
        assertThat(order, notNullValue());
    }
}
