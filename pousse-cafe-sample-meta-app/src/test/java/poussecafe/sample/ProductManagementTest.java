package poussecafe.sample;

import java.util.List;
import org.junit.Test;
import poussecafe.environment.BoundedContext;
import poussecafe.sample.command.AddUnits;
import poussecafe.sample.command.CreateProduct;
import poussecafe.sample.domain.Product;
import poussecafe.sample.domain.ProductKey;
import poussecafe.sample.process.ProductManagement;
import poussecafe.test.PousseCafeTest;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class ProductManagementTest extends PousseCafeTest {

    private ProductKey productKey;

    private AddUnits addUnits;

    @Override
    protected List<BoundedContext> boundedContexts() {
        return asList(SampleBoundedContextDefinition.configure().defineAndImplementDefault().build());
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
        productManagement.createProduct(new CreateProduct(productKey));
    }

    private ProductManagement productManagement;

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
        productManagement.addUnits(addUnits);
    }

    private void thenProductHasAddedUnits() {
        Product product = find(Product.class, productKey);
        assertThat(product.getAvailableUnits(), is(addUnits.getUnits()));
        assertThat(product.getTotalUnits(), is(addUnits.getUnits()));
    }
}
