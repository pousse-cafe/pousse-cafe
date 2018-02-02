package poussecafe.sample.domain.mongo;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;
import poussecafe.sample.domain.CustomerKey;
import poussecafe.sample.domain.MessageKey;
import poussecafe.spring.mongo.storage.MongoDataAccess;

public class MessageDataAccess extends MongoDataAccess<MessageKey, MessageData, String> implements poussecafe.sample.domain.MessageDataAccess<MessageData> {

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
