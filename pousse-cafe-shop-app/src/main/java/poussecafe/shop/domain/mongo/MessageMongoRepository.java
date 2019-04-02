package poussecafe.shop.domain.mongo;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import poussecafe.shop.domain.CustomerId;

public interface MessageMongoRepository extends MongoRepository<MessageData, String> {

    List<MessageData> findByCustomerId(CustomerId customerId);

}
