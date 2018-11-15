package poussecafe.sample.domain.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import poussecafe.sample.adapters.storage.SerializableOrderKey;

public interface OrderMongoRepository extends MongoRepository<OrderData, SerializableOrderKey> {

}
