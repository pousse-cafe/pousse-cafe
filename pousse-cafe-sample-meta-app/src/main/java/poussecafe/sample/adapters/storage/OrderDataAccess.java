package poussecafe.sample.adapters.storage;

import poussecafe.contextconfigurer.DataAccessImplementation;
import poussecafe.sample.domain.Order;
import poussecafe.sample.domain.OrderKey;
import poussecafe.storage.internal.InternalDataAccess;
import poussecafe.storage.internal.InternalStorage;

@DataAccessImplementation(
    aggregateRoot = Order.class,
    dataImplementation = OrderData.class,
    storageName = InternalStorage.NAME
)
public class OrderDataAccess extends InternalDataAccess<OrderKey, Order.Data> {

}
