package poussecafe.sample.app;

import org.springframework.context.annotation.Configuration;
import poussecafe.configuration.AggregateConfiguration;
import poussecafe.data.memory.InMemoryDataAccess;
import poussecafe.data.memory.InMemoryStorableDataFactory;
import poussecafe.sample.domain.Order;
import poussecafe.sample.domain.Order.OrderData;
import poussecafe.sample.domain.OrderFactory;
import poussecafe.sample.domain.OrderKey;
import poussecafe.sample.domain.OrderRepository;
import poussecafe.storable.StorableDataAccess;
import poussecafe.storable.StorableDataFactory;

@Configuration
public class OrderConfiguration
extends AggregateConfiguration<OrderKey, Order, OrderData, OrderFactory, OrderRepository> {

    public OrderConfiguration() {
        super(Order.class, OrderFactory.class, OrderRepository.class);
    }

    @Override
    protected StorableDataFactory<OrderData> aggregateDataFactory() {
        return new InMemoryStorableDataFactory<>(OrderData.class);
    }

    @Override
    protected StorableDataAccess<OrderKey, OrderData> dataAccess() {
        return new InMemoryDataAccess<>(OrderData.class);
    }

}
