package poussecafe.shop.domain.mongo;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;
import poussecafe.discovery.DataAccessImplementation;
import poussecafe.shop.domain.CustomerId;
import poussecafe.shop.domain.Message;
import poussecafe.shop.domain.MessageId;
import poussecafe.spring.mongo.storage.MongoDataAccess;
import poussecafe.spring.mongo.storage.SpringMongoDbStorage;

@DataAccessImplementation(
        aggregateRoot = Message.class,
        dataImplementation = MessageData.class,
        storageName = SpringMongoDbStorage.NAME
)
public class MessageDataAccess extends MongoDataAccess<MessageId, MessageData, String> implements poussecafe.shop.domain.MessageDataAccess<MessageData> {

    @Autowired
    private MessageMongoRepository repository;

    @Override
    protected String convertId(MessageId id) {
        return id.stringValue();
    }

    @Override
    protected MongoRepository<MessageData, String> mongoRepository() {
        return repository;
    }

    @Override
    public List<MessageData> findByCustomer(CustomerId customerId) {
        return repository.findByCustomerId(customerId);
    }

}
