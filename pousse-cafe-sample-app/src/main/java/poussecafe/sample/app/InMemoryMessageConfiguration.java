package poussecafe.sample.app;

import org.springframework.context.annotation.Configuration;
import poussecafe.data.memory.InMemoryDataAccess;
import poussecafe.data.memory.InMemoryDataFactory;
import poussecafe.sample.configuration.MessageConfiguration;
import poussecafe.sample.domain.Message;

@Configuration
public class InMemoryMessageConfiguration extends MessageConfiguration {

    public InMemoryMessageConfiguration() {
        setDataFactory(new InMemoryDataFactory<>(Message.Data.class));
        setDataAccess(new InMemoryDataAccess<>(Message.Data.class));
    }

}
