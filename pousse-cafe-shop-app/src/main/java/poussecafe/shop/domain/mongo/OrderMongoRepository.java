package poussecafe.shop.domain.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import poussecafe.shop.adapters.storage.SerializableOrderKey;

public interface OrderMongoRepository extends MongoRepository<OrderData, SerializableOrderKey> {

}
