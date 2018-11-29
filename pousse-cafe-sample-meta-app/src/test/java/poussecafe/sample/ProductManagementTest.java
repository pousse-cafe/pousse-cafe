package poussecafe.sample;

import java.util.List;
import org.junit.Test;
import poussecafe.context.BoundedContext;
import poussecafe.sample.command.AddUnits;
import poussecafe.sample.command.CreateProduct;
import poussecafe.sample.domain.Product;
import poussecafe.sample.domain.ProductKey;
import poussecafe.sample.process.ProductManagement;
import poussecafe.test.MetaApplicationTest;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class ProductManagementTest extends MetaApplicationTest {

    private ProductKey productKey;

    private AddUnits addUnits;

    @Override
    protected List<BoundedContext> testBundle() {
        return asList(new SampleMetaAppBoundedContextDefinition().withDefaultImplementation().build());
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
        context().getDomainProcess(ProductManagement.class).createProduct(new CreateProduct(productKey));
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
        context().getDomainProcess(ProductManagement.class).addUnits(addUnits);
    }

    private void thenProductHasAddedUnits() {
        Product product = find(Product.class, productKey);
        assertThat(product.getAvailableUnits(), is(addUnits.getUnits()));
        assertThat(product.getTotalUnits(), is(addUnits.getUnits()));
    }
}
