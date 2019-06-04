package poussecafe.shop.domain.mongo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;
import poussecafe.discovery.DataAccessImplementation;
import poussecafe.shop.domain.Customer;
import poussecafe.shop.domain.CustomerId;
import poussecafe.spring.mongo.storage.MongoDataAccess;
import poussecafe.spring.mongo.storage.SpringMongoDbStorage;

@DataAccessImplementation(
        aggregateRoot = Customer.class,
        dataImplementation = CustomerData.class,
        storageName = SpringMongoDbStorage.NAME
)public class CustomerDataAccess extends MongoDataAccess<CustomerId, CustomerData, String> implements poussecafe.shop.domain.CustomerDataAccess<CustomerData> {

    @Autowired
    private CustomerMongoRepository repository;

    @Override
    protected String convertId(CustomerId id) {
        return id.stringValue();
    }

    @Override
    protected MongoRepository<CustomerData, String> mongoRepository() {
        return repository;
    }
}
