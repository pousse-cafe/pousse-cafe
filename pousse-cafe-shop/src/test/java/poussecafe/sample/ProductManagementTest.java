package poussecafe.sample;

import org.junit.Test;
import poussecafe.shop.command.AddUnits;
import poussecafe.shop.command.CreateProduct;
import poussecafe.shop.domain.Product;
import poussecafe.shop.domain.ProductId;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class ProductManagementTest extends ShopTest {

    private ProductId productId;

    @Test
    public void productCanBeCreated() {
        givenProductId();
        whenSubmittingProductCreationCommand();
        thenProductCreated();
    }

    private void givenProductId() {
        productId = new ProductId("product-id");
    }

    private void whenSubmittingProductCreationCommand() {
        createProduct();
    }

    protected void createProduct() {
        CreateProduct command = newCommand(CreateProduct.class);
        command.productId().value(productId);
        submitCommand(command);
    }

    private void thenProductCreated() {
        assertThat(find(Product.class, productId), notNullValue());
    }

    @Test
    public void unitsCanBeAdded() {
        givenProductWithZeroUnits();
        whenSubmittingAddUnitsCommand();
        thenProductHasAddedUnits();
    }

    private void givenProductWithZeroUnits() {
        givenProductId();
        createProduct();
    }

    private void whenSubmittingAddUnitsCommand() {
        addUnits = newCommand(AddUnits.class);
        addUnits.productId().value(productId);
        addUnits.units().value(1);
        submitCommand(addUnits);
    }

    private AddUnits addUnits;

    private void thenProductHasAddedUnits() {
        Product product = find(Product.class, productId);
        assertThat(product.attributes().availableUnits().value(), is(addUnits.units().value()));
        assertThat(product.attributes().totalUnits().value(), is(addUnits.units().value()));
    }
}
