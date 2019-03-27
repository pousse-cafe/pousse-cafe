package poussecafe.shop.domain.mongo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;
import poussecafe.shop.adapters.storage.OrderKeyData;
import poussecafe.shop.domain.OrderKey;
import poussecafe.spring.mongo.storage.MongoDataAccess;

public class OrderDataAccess extends MongoDataAccess<OrderKey, OrderData, OrderKeyData> {

    @Autowired
    private OrderMongoRepository repository;

    @Override
    protected OrderKeyData convertKey(OrderKey key) {
        return OrderKeyData.adapt(key);
    }

    @Override
    protected MongoRepository<OrderData, OrderKeyData> mongoRepository() {
        return repository;
    }

}
