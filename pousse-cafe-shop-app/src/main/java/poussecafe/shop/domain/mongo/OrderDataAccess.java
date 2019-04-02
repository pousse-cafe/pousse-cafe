package poussecafe.shop.domain.mongo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;
import poussecafe.shop.adapters.storage.OrderIdData;
import poussecafe.shop.domain.OrderId;
import poussecafe.spring.mongo.storage.MongoDataAccess;

public class OrderDataAccess extends MongoDataAccess<OrderId, OrderData, OrderIdData> {

    @Autowired
    private OrderMongoRepository repository;

    @Override
    protected OrderIdData convertId(OrderId id) {
        return OrderIdData.adapt(id);
    }

    @Override
    protected MongoRepository<OrderData, OrderIdData> mongoRepository() {
        return repository;
    }

}
