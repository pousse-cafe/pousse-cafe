package poussecafe.shop.adapters.storage;

import poussecafe.discovery.DataAccessImplementation;
import poussecafe.shop.domain.Customer;
import poussecafe.shop.domain.CustomerId;
import poussecafe.storage.internal.InternalDataAccess;
import poussecafe.storage.internal.InternalStorage;

@DataAccessImplementation(
    aggregateRoot = Customer.class,
    dataImplementation = CustomerData.class,
    storageName = InternalStorage.NAME
)
public class CustomerDataAccess extends InternalDataAccess<CustomerId, Customer.Attributes> {

}
