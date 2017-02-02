package poussecafe.sample.configuration;

import poussecafe.configuration.AggregateConfiguration;
import poussecafe.sample.domain.Order;
import poussecafe.sample.domain.Order.Data;
import poussecafe.sample.domain.OrderFactory;
import poussecafe.sample.domain.OrderKey;
import poussecafe.sample.domain.OrderRepository;

public class OrderConfiguration
extends AggregateConfiguration<OrderKey, Order, Data, OrderFactory, OrderRepository> {

    public OrderConfiguration() {
        super(Order.class, OrderFactory.class, OrderRepository.class);
    }

}
