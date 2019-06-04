package poussecafe.shop.domain.mongo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;
import poussecafe.discovery.DataAccessImplementation;
import poussecafe.shop.adapters.storage.OrderIdData;
import poussecafe.shop.domain.Order;
import poussecafe.shop.domain.OrderId;
import poussecafe.spring.mongo.storage.MongoDataAccess;
import poussecafe.spring.mongo.storage.SpringMongoDbStorage;

@DataAccessImplementation(
        aggregateRoot = Order.class,
        dataImplementation = OrderData.class,
        storageName = SpringMongoDbStorage.NAME
)
public class OrderDataAccess extends MongoDataAccess<OrderId, OrderData, OrderIdData> implements poussecafe.shop.domain.OrderDataAccess<OrderData> {

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
