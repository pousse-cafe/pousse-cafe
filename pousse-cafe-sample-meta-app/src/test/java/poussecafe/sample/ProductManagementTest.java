package poussecafe.sample;

import org.junit.Test;
import poussecafe.sample.command.AddUnits;
import poussecafe.sample.command.CreateProduct;
import poussecafe.sample.domain.Product;
import poussecafe.sample.domain.ProductFactory;
import poussecafe.sample.domain.ProductKey;
import poussecafe.sample.domain.ProductRepository;
import poussecafe.sample.workflow.ProductManagement;
import poussecafe.storable.StorableDefinition;
import poussecafe.test.MetaApplicationTest;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class ProductManagementTest extends MetaApplicationTest {

    private ProductKey productKey;

    private AddUnits addUnits;

    @Override
    protected void registerComponents() {
        context().environment().defineStorable(new StorableDefinition.Builder()
                .withStorableClass(Product.class)
                .withFactoryClass(ProductFactory.class)
                .withRepositoryClass(ProductRepository.class)
                .build());

        context().environment().defineProcess(ProductManagement.class);
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
        context().getProcess(ProductManagement.class).createProduct(new CreateProduct(productKey));
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
        context().getProcess(ProductManagement.class).addUnits(addUnits);
    }

    private void thenProductHasAddedUnits() {
        Product product = find(Product.class, productKey);
        assertThat(product.getAvailableUnits(), is(addUnits.getUnits()));
        assertThat(product.getTotalUnits(), is(addUnits.getUnits()));
    }
}
