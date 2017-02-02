package poussecafe.sample.app;

import org.junit.Test;
import poussecafe.sample.command.AddUnits;
import poussecafe.sample.command.CreateCustomer;
import poussecafe.sample.command.CreateProduct;
import poussecafe.sample.command.PlaceOrder;
import poussecafe.sample.configuration.CustomerConfiguration;
import poussecafe.sample.configuration.OrderConfiguration;
import poussecafe.sample.configuration.ProductConfiguration;
import poussecafe.sample.domain.Customer;
import poussecafe.sample.domain.CustomerKey;
import poussecafe.sample.domain.Order;
import poussecafe.sample.domain.OrderDescription;
import poussecafe.sample.domain.OrderKey;
import poussecafe.sample.domain.Product;
import poussecafe.sample.domain.ProductKey;
import poussecafe.sample.workflow.CustomerCreation;
import poussecafe.sample.workflow.OrderManagement;
import poussecafe.sample.workflow.ProductManagement;
import poussecafe.test.TestConfigurationBuilder;
import poussecafe.test.WorkflowTest;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class PlacingOrderTest extends WorkflowTest {

    private CustomerCreation customerCreation;

    private ProductManagement productManagement;

    private OrderManagement orderManagement;

    private CustomerKey customerKey;

    private ProductKey productKey;

    private OrderDescription description;

    @Override
    protected void registerActors() {
        configuration.registerAggregateConfiguration(
                new TestConfigurationBuilder()
                .withConfiguration(new CustomerConfiguration())
                .withData(Customer.Data.class)
                .build());

        configuration.registerAggregateConfiguration(new TestConfigurationBuilder()
                .withConfiguration(new ProductConfiguration())
                .withData(Product.Data.class)
                .build());

        configuration.registerAggregateConfiguration(new TestConfigurationBuilder()
                .withConfiguration(new OrderConfiguration())
                .withData(Order.Data.class)
                .build());

        productManagement = new ProductManagement();
        configuration.registerWorkflow(productManagement);

        customerCreation = new CustomerCreation();
        configuration.registerWorkflow(customerCreation);

        orderManagement = new OrderManagement();
        configuration.registerWorkflow(orderManagement);
    }

    @Test
    public void placingOrderCreatesOrder() {
        givenContext();
        givenCustomer();
        givenProductWithUnits(10);
        whenPlacingOrder();
        thenOrderCreated();
    }

    private void givenCustomer() {
        customerKey = new CustomerKey();
        processAndAssertSuccess(new CreateCustomer(customerKey));
    }

    private void givenProductWithUnits(int units) {
        productKey = new ProductKey("product-1");
        processAndAssertSuccess(new CreateProduct(productKey));
        if (units > 0) {
            processAndAssertSuccess(new AddUnits(productKey, units));
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
        givenContext();
        givenCustomer();
        givenProductWithUnits(0);
        whenPlacingOrder();
        thenNoOrderCreated();
    }

    private void thenNoOrderCreated() {
        assertThat(find(Order.class, orderKey()), nullValue());
    }
}
