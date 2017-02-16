package poussecafe.sample.domain;

import poussecafe.domain.Factory;

public class ProductFactory extends Factory<ProductKey, Product, Product.Data> {

    public Product buildProductWithNoStock(ProductKey productKey) {
        Product product = newAggregateWithKey(productKey);
        product.setTotalUnits(0);
        product.setAvailableUnits(0);
        return product;
    }

    @Override
    protected Product newAggregate() {
        return new Product();
    }
}
