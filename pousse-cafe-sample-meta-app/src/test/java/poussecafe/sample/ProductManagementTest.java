package poussecafe.sample;

import org.junit.Test;
import poussecafe.sample.command.AddUnits;
import poussecafe.sample.command.CreateProduct;
import poussecafe.sample.configuration.ProductConfiguration;
import poussecafe.sample.domain.Product;
import poussecafe.sample.domain.ProductKey;
import poussecafe.sample.workflow.ProductManagement;
import poussecafe.test.MetaApplicationTest;
import poussecafe.test.TestConfigurationBuilder;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class ProductManagementTest extends MetaApplicationTest {

    private ProductKey productKey;

    private AddUnits addUnits;

    @Override
    protected void registerComponents() {
        configuration.registerAggregate(new TestConfigurationBuilder()
                .withConfiguration(new ProductConfiguration())
                .withData(Product.Data.class)
                .build());

        configuration.registerWorkflow(new ProductManagement());
    }

    @Test
    public void productCanBeCreated() {
        givenProductKey();
        whenSubmittingProductCreationCommand();
        thenProductCreated();
    }

    private void givenProductKey() {
        productKey = new ProductKey("product-id");
    }

    private void whenSubmittingProductCreationCommand() {
        createProduct();
    }

    protected void createProduct() {
        processAndAssertSuccess(new CreateProduct(productKey));
    }

    private void thenProductCreated() {
        assertThat(find(Product.class, productKey), notNullValue());
    }

    @Test
    public void unitsCanBeAdded() {
        givenProductWithZeroUnits();
        whenSubmittingAddUnitsCommand();
        thenProductHasAddedUnits();
    }

    private void givenProductWithZeroUnits() {
        givenProductKey();
        createProduct();
    }

    private void whenSubmittingAddUnitsCommand() {
        addUnits = new AddUnits(productKey, 10);
        processAndAssertSuccess(addUnits);
    }

    private void thenProductHasAddedUnits() {
        Product product = find(Product.class, productKey);
        assertThat(product.getAvailableUnits(), is(addUnits.getUnits()));
        assertThat(product.getTotalUnits(), is(addUnits.getUnits()));
    }
}
