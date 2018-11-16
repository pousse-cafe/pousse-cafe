package poussecafe.sample.app;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import poussecafe.context.MetaApplicationContext;
import poussecafe.journal.MongoJournalBoundedContext;

@Configuration
@ComponentScan(basePackages = { "poussecafe.spring" })
public class AppConfiguration {

    @Bean
    public MetaApplicationContext pousseCafeApplicationContext() {
        MetaApplicationContext context = new MetaApplicationContext();
        context.loadBoundedContext(new MongoJournalBoundedContext());
        context.loadBoundedContext(new MongoSampleMetaAppBoundedContext());
        context.start();
        return context;
    }
}
