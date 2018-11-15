package poussecafe.sample.adapters.storage;

import poussecafe.sample.domain.Order;
import poussecafe.sample.domain.OrderKey;
import poussecafe.storage.memory.InMemoryDataAccess;

public class OrderDataAccess extends InMemoryDataAccess<OrderKey, Order.Data> {

}
