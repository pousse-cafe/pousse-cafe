package poussecafe.shop.domain.mongo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;
import poussecafe.shop.domain.CustomerKey;
import poussecafe.spring.mongo.storage.MongoDataAccess;

public class CustomerDataAccess extends MongoDataAccess<CustomerKey, CustomerData, String> {

    @Autowired
    private CustomerMongoRepository repository;

    @Override
    protected String convertKey(CustomerKey key) {
        return key.getValue();
    }

    @Override
    protected MongoRepository<CustomerData, String> mongoRepository() {
        return repository;
    }
}
