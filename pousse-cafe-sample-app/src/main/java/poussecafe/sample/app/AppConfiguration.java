package poussecafe.sample.app;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import poussecafe.context.MessagingAndStorage;
import poussecafe.context.MetaApplicationContext;
import poussecafe.journal.JournalBoundedContextDefinition;
import poussecafe.messaging.internal.InternalMessaging;
import poussecafe.sample.SampleMetaAppBoundedContextDefinition;
import poussecafe.spring.mongo.storage.SpringMongoDbStorage;

@Configuration
@ComponentScan(basePackages = { "poussecafe.spring" })
public class AppConfiguration {

    @Bean
    public MetaApplicationContext pousseCafeApplicationContext() {
        MetaApplicationContext context = new MetaApplicationContext();
        MessagingAndStorage messagingAndStorage = new MessagingAndStorage(InternalMessaging.instance(),
                SpringMongoDbStorage.instance());
        context.addBoundedContext(new JournalBoundedContextDefinition().implement()
                .messagingAndStorage(messagingAndStorage)
                .build());
        context.addBoundedContext(new SampleMetaAppBoundedContextDefinition().implement()
                .messagingAndStorage(messagingAndStorage)
                .build());
        context.start();
        return context;
    }
}
