package poussecafe.sample.app;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import poussecafe.context.MetaApplicationContext;

@Configuration
@ComponentScan(basePackages = { "poussecafe.spring" })
public class AppConfiguration {

    @Bean
    public MetaApplicationContext pousseCafeApplicationContext() {
        MetaApplicationContext context = new MetaApplicationContext();
        context.loadBundle(new SampleMetaAppMongoBundle());
        context.start();
        return context;
    }
}
