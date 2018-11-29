package poussecafe.sample.adapters.storage;

import poussecafe.sample.domain.Customer;
import poussecafe.sample.domain.CustomerKey;
import poussecafe.storage.DataAccessImplementation;
import poussecafe.storage.internal.InternalDataAccess;
import poussecafe.storage.internal.InternalStorage;

@DataAccessImplementation(
    aggregateRoot = Customer.class,
    dataImplementation = CustomerData.class,
    storageName = InternalStorage.NAME
)
public class CustomerDataAccess extends InternalDataAccess<CustomerKey, Customer.Data> {

}
