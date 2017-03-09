package poussecafe.sample.configuration;

import poussecafe.configuration.AggregateConfiguration;
import poussecafe.sample.domain.Product;
import poussecafe.sample.domain.Product.Data;
import poussecafe.sample.domain.ProductFactory;
import poussecafe.sample.domain.ProductKey;
import poussecafe.sample.domain.ProductRepository;

public class ProductConfiguration
extends AggregateConfiguration<ProductKey, Product, Data, ProductFactory, ProductRepository> {

    public ProductConfiguration() {
        super(Product.class, Product.Data.class, ProductFactory.class, ProductRepository.class);
    }
}
