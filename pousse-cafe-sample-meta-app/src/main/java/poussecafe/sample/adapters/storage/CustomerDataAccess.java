package poussecafe.sample.adapters.storage;

import poussecafe.sample.domain.Customer;
import poussecafe.sample.domain.CustomerKey;
import poussecafe.storage.memory.InMemoryDataAccess;

public class CustomerDataAccess extends InMemoryDataAccess<CustomerKey, Customer.Data> {

}
