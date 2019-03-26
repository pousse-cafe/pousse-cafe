package poussecafe.shop;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import poussecafe.journal.Journal;
import poussecafe.messaging.internal.InternalMessaging;
import poussecafe.runtime.MessagingAndStorage;
import poussecafe.runtime.Runtime;
import poussecafe.shop.Shop;
import poussecafe.spring.mongo.storage.SpringMongoDbStorage;

@Configuration
@ComponentScan(basePackages = { "poussecafe.spring" })
public class AppConfiguration {

    @Bean
    public Runtime pousseCafeRuntime() {
        MessagingAndStorage messagingAndStorage = new MessagingAndStorage(InternalMessaging.instance(),
                SpringMongoDbStorage.instance());

        Runtime context = new Runtime.Builder()
            .withBoundedContext(Journal.configure()
                    .defineThenImplement()
                    .messagingAndStorage(messagingAndStorage)
                    .build())
            .withBoundedContext(Shop.configure()
                    .defineThenImplement()
                    .messagingAndStorage(messagingAndStorage)
                    .build())
            .build();

        context.start();

        return context;
    }
}
