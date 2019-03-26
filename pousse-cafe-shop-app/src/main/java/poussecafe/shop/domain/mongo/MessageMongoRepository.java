package poussecafe.shop.domain.mongo;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import poussecafe.shop.domain.CustomerKey;

public interface MessageMongoRepository extends MongoRepository<MessageData, String> {

    List<MessageData> findByCustomerKey(CustomerKey customerKey);

}
