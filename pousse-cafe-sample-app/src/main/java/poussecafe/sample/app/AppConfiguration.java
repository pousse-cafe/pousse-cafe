package poussecafe.sample.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import poussecafe.configuration.MetaApplicationContext;
import poussecafe.messaging.CommandProcessor;
import poussecafe.sample.domain.Product;
import poussecafe.sample.domain.ProductRepository;

@Configuration
@ComponentScan(basePackages = { "poussecafe.sample" })
public class AppConfiguration {

    @Autowired
    private PousseCafeConfiguration pousseCafeConfiguration;

    @Bean
    public MetaApplicationContext pousseCafeApplicationContext() {
        return new MetaApplicationContext(pousseCafeConfiguration);
    }

    @Bean
    public CommandProcessor commandProcessor() {
        return pousseCafeApplicationContext().getCommandProcessor();
    }

    @Bean
    public ProductRepository productRepository() {
        return (ProductRepository) pousseCafeApplicationContext().getStorableServices(Product.class).getRepository();
    }
}
