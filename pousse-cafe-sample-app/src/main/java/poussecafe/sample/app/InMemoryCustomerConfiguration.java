package poussecafe.sample.app;

import org.springframework.context.annotation.Configuration;
import poussecafe.data.memory.InMemoryDataAccess;
import poussecafe.data.memory.InMemoryDataFactory;
import poussecafe.sample.configuration.CustomerConfiguration;
import poussecafe.sample.domain.Customer;

@Configuration
public class InMemoryCustomerConfiguration extends CustomerConfiguration {

    public InMemoryCustomerConfiguration() {
        setDataFactory(new InMemoryDataFactory<>(Customer.Data.class));
        setDataAccess(new InMemoryDataAccess<>(Customer.Data.class));
    }

}
