package poussecafe.sample;

import org.junit.Test;
import poussecafe.sample.command.PlaceOrder;
import poussecafe.sample.domain.CustomerKey;
import poussecafe.sample.domain.Order;
import poussecafe.sample.domain.OrderDescription;
import poussecafe.sample.domain.OrderFactory;
import poussecafe.sample.domain.OrderKey;
import poussecafe.sample.domain.OrderRepository;
import poussecafe.sample.domain.Product;
import poussecafe.sample.domain.ProductFactory;
import poussecafe.sample.domain.ProductKey;
import poussecafe.sample.domain.ProductRepository;
import poussecafe.sample.workflow.OrderPlacement;
import poussecafe.storable.StorableDefinition;
import poussecafe.test.MetaApplicationTest;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class OrderManagementTest extends MetaApplicationTest {

    private CustomerKey customerKey;

    private ProductKey productKey;

    private OrderDescription description;

    @Override
    protected void registerComponents() {
        context().environment().defineStorable(new StorableDefinition.Builder()
                .withStorableClass(Product.class)
                .withFactoryClass(ProductFactory.class)
                .withRepositoryClass(ProductRepository.class)
                .build());

        context().environment().defineStorable(new StorableDefinition.Builder()
                .withStorableClass(Order.class)
                .withFactoryClass(OrderFactory.class)
                .withRepositoryClass(OrderRepository.class)
                .build());

        context().environment().defineProcess(OrderPlacement.class);
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
        context().getProcess(OrderPlacement.class).placeOrder(new PlaceOrder(productKey, description));
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
