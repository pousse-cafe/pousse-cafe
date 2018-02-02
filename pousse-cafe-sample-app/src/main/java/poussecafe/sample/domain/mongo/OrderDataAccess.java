package poussecafe.sample.domain.mongo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;
import poussecafe.sample.domain.OrderKey;
import poussecafe.sample.domain.memory.SerializableOrderKey;
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
