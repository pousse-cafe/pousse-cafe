package poussecafe.shop.domain.mongo;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;
import poussecafe.shop.domain.CustomerKey;
import poussecafe.shop.domain.MessageKey;
import poussecafe.spring.mongo.storage.MongoDataAccess;

public class MessageDataAccess extends MongoDataAccess<MessageKey, MessageData, String> implements poussecafe.shop.domain.MessageDataAccess<MessageData> {

    @Autowired
    private MessageMongoRepository repository;

    @Override
    protected String convertKey(MessageKey key) {
        return key.getValue();
    }

    @Override
    protected MongoRepository<MessageData, String> mongoRepository() {
        return repository;
    }

    @Override
    public List<MessageData> findByCustomer(CustomerKey customerKey) {
        return repository.findByCustomerKey(customerKey);
    }

}
