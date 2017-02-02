package poussecafe.sample.domain;

import poussecafe.domain.Repository;

public class ProductRepository extends Repository<Product, ProductKey, Product.Data> {

    @Override
    protected Product newAggregate() {
        return new Product();
    }

}
