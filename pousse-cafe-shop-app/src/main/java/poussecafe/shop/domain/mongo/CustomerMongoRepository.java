package poussecafe.shop.domain.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface CustomerMongoRepository extends MongoRepository<CustomerData, String> {

}
