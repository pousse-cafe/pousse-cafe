package poussecafe.sample;

import org.junit.Test;
import poussecafe.shop.command.AddUnits;
import poussecafe.shop.command.CreateProduct;
import poussecafe.shop.domain.Product;
import poussecafe.shop.domain.ProductKey;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class ProductManagementTest extends ShopTest {

    private ProductKey productKey;

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
        CreateProduct command = newCommand(CreateProduct.class);
        command.productKey().value(productKey);
        submitCommand(command);
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
        addUnits = newCommand(AddUnits.class);
        addUnits.productKey().value(productKey);
        addUnits.units().value(1);
        submitCommand(addUnits);
    }

    private AddUnits addUnits;

    private void thenProductHasAddedUnits() {
        Product product = find(Product.class, productKey);
        assertThat(product.attributes().availableUnits().value(), is(addUnits.units().value()));
        assertThat(product.attributes().totalUnits().value(), is(addUnits.units().value()));
    }
}
