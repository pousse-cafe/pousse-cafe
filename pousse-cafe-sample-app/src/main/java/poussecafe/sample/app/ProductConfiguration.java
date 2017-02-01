package poussecafe.sample.app;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import poussecafe.configuration.AggregateConfiguration;
import poussecafe.data.memory.InMemoryDataAccess;
import poussecafe.data.memory.InMemoryStorableDataFactory;
import poussecafe.sample.domain.Product;
import poussecafe.sample.domain.Product.ProductData;
import poussecafe.sample.domain.ProductFactory;
import poussecafe.sample.domain.ProductKey;
import poussecafe.sample.domain.ProductRepository;
import poussecafe.storable.StorableDataAccess;
import poussecafe.storable.StorableDataFactory;

@Configuration
public class ProductConfiguration
extends AggregateConfiguration<ProductKey, Product, ProductData, ProductFactory, ProductRepository> {

    public ProductConfiguration() {
        super(Product.class, ProductFactory.class, ProductRepository.class);
    }

    @Override
    protected StorableDataFactory<ProductData> aggregateDataFactory() {
        return new InMemoryStorableDataFactory<>(ProductData.class);
    }

    @Override
    protected StorableDataAccess<ProductKey, ProductData> dataAccess() {
        return new InMemoryDataAccess<>(ProductData.class);
    }

    @Bean
    public ProductRepository productRepository() {
        return repository().get();
    }
}
