package poussecafe.shop.domain;

import poussecafe.domain.Factory;

public class ProductFactory extends Factory<ProductKey, Product, Product.Attributes> {

    public Product buildProductWithNoStock(ProductKey productKey) {
        Product product = newAggregateWithKey(productKey);
        product.setTotalUnits(0);
        product.setAvailableUnits(0);
        return product;
    }
}
