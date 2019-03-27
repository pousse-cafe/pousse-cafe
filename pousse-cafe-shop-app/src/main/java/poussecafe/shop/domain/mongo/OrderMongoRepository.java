package poussecafe.shop.domain.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import poussecafe.shop.adapters.storage.OrderKeyData;

public interface OrderMongoRepository extends MongoRepository<OrderData, OrderKeyData> {

}
