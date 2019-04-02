package poussecafe.shop.adapters.storage;

import java.util.List;
import poussecafe.discovery.DataAccessImplementation;
import poussecafe.shop.domain.CustomerId;
import poussecafe.shop.domain.Message;
import poussecafe.shop.domain.MessageDataAccess;
import poussecafe.shop.domain.MessageId;
import poussecafe.storage.internal.InternalDataAccess;
import poussecafe.storage.internal.InternalStorage;

import static java.util.Arrays.asList;

@DataAccessImplementation(
    aggregateRoot = Message.class,
    dataImplementation = MessageData.class,
    storageName = InternalStorage.NAME
)
public class MessageInternalDataAccess extends InternalDataAccess<MessageId, MessageData> implements MessageDataAccess<MessageData> {

    @Override
    protected List<Object> extractIndexedData(MessageData data) {
        return asList(data.customerId().value());
    }

    @Override
    public List<MessageData> findByCustomer(CustomerId customerId) {
        return findBy(customerId);
    }

}
