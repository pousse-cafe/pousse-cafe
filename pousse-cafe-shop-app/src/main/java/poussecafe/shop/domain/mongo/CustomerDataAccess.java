package poussecafe.shop.domain.mongo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;
import poussecafe.shop.domain.CustomerId;
import poussecafe.spring.mongo.storage.MongoDataAccess;

public class CustomerDataAccess extends MongoDataAccess<CustomerId, CustomerData, String> {

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
