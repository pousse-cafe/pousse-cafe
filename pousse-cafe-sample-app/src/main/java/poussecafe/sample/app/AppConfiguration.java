package poussecafe.sample.app;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import poussecafe.configuration.MetaApplicationContext;

@Configuration
@ComponentScan(basePackages = { "poussecafe.sample" })
public class AppConfiguration {

    @Bean
    public MetaApplicationContext pousseCafeApplicationContext() {
        MetaApplicationContext context = new MetaApplicationContext();
        return context;
    }
}
