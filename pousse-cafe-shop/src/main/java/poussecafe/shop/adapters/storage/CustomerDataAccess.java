package poussecafe.shop.adapters.storage;

import poussecafe.discovery.DataAccessImplementation;
import poussecafe.shop.domain.Customer;
import poussecafe.shop.domain.CustomerKey;
import poussecafe.storage.internal.InternalDataAccess;
import poussecafe.storage.internal.InternalStorage;

@DataAccessImplementation(
    aggregateRoot = Customer.class,
    dataImplementation = CustomerData.class,
    storageName = InternalStorage.NAME
)
public class CustomerDataAccess extends InternalDataAccess<CustomerKey, Customer.Attributes> {

}
