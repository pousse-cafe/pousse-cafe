package poussecafe.sample.domain;

import poussecafe.domain.Repository;

public class ProductRepository extends Repository<Product, ProductKey, Product.ProductData> {

    @Override
    protected Product newAggregate() {
        return new Product();
    }

}
