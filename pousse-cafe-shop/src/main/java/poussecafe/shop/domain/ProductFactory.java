package poussecafe.shop.domain;

import poussecafe.discovery.MessageListener;
import poussecafe.domain.Factory;
import poussecafe.shop.command.CreateProduct;

public class ProductFactory extends Factory<ProductId, Product, Product.Attributes> {

    /**
     * @process ProductManagement
     */
    @MessageListener
    public Product buildProductWithNoStock(CreateProduct command) {
        Product product = newAggregateWithId(command.productId().value());
        product.attributes().totalUnits().value(0);
        product.attributes().availableUnits().value(0);
        return product;
    }
}
