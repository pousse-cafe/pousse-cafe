package poussecafe.shop.adapters.storage;

import poussecafe.discovery.DataAccessImplementation;
import poussecafe.shop.domain.Order;
import poussecafe.shop.domain.OrderKey;
import poussecafe.storage.internal.InternalDataAccess;
import poussecafe.storage.internal.InternalStorage;

@DataAccessImplementation(
    aggregateRoot = Order.class,
    dataImplementation = OrderData.class,
    storageName = InternalStorage.NAME
)
public class OrderDataAccess extends InternalDataAccess<OrderKey, Order.Attributes> {

}
