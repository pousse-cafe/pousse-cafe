package poussecafe.sample.app;

import org.springframework.context.annotation.Configuration;
import poussecafe.data.memory.InMemoryDataAccess;
import poussecafe.data.memory.InMemoryDataFactory;
import poussecafe.sample.configuration.OrderConfiguration;
import poussecafe.sample.domain.Order;

@Configuration
public class InMemoryOrderConfiguration extends OrderConfiguration {

    public InMemoryOrderConfiguration() {
        setDataFactory(new InMemoryDataFactory<>(Order.Data.class));
        setDataAccess(new InMemoryDataAccess<>(Order.Data.class));
    }

}
