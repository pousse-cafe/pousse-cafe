package poussecafe.sample.app;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import poussecafe.data.memory.InMemoryDataAccess;
import poussecafe.data.memory.InMemoryDataFactory;
import poussecafe.sample.configuration.ProductConfiguration;
import poussecafe.sample.domain.Product;
import poussecafe.sample.domain.ProductRepository;

@Configuration
public class InMemoryProductConfiguration extends ProductConfiguration {

    public InMemoryProductConfiguration() {
        setDataFactory(new InMemoryDataFactory<>(Product.Data.class));
        setDataAccess(new InMemoryDataAccess<>(Product.Data.class));
    }

    @Bean
    public ProductRepository productRepository() {
        return repository().get();
    }
}
