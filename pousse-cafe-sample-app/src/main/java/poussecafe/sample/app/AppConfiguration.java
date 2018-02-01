package poussecafe.sample.app;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import poussecafe.context.MetaApplicationContext;
import poussecafe.sample.SampleMetaAppBundle;

@Configuration
@ComponentScan(basePackages = { "poussecafe.sample" })
public class AppConfiguration {

    @Bean
    public MetaApplicationContext pousseCafeApplicationContext() {
        MetaApplicationContext context = new MetaApplicationContext();
        context.loadBundle(new SampleMetaAppBundle());
        context.start();
        return context;
    }
}
