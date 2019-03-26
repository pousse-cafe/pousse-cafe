package poussecafe.shop.domain.mongo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;
import poussecafe.shop.adapters.storage.SerializableOrderKey;
import poussecafe.shop.domain.OrderKey;
import poussecafe.spring.mongo.storage.MongoDataAccess;

public class OrderDataAccess extends MongoDataAccess<OrderKey, OrderData, SerializableOrderKey> {

    @Autowired
    private OrderMongoRepository repository;

    @Override
    protected SerializableOrderKey convertKey(OrderKey key) {
        return new SerializableOrderKey(key);
    }

    @Override
    protected MongoRepository<OrderData, SerializableOrderKey> mongoRepository() {
        return repository;
    }

}
