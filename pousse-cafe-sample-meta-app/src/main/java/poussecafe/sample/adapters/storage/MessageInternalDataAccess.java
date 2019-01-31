package poussecafe.sample.adapters.storage;

import java.util.List;
import poussecafe.contextconfigurer.DataAccessImplementation;
import poussecafe.sample.domain.CustomerKey;
import poussecafe.sample.domain.Message;
import poussecafe.sample.domain.MessageDataAccess;
import poussecafe.sample.domain.MessageKey;
import poussecafe.storage.internal.InternalDataAccess;
import poussecafe.storage.internal.InternalStorage;

import static java.util.Arrays.asList;

@DataAccessImplementation(
    aggregateRoot = Message.class,
    dataImplementation = MessageData.class,
    storageName = InternalStorage.NAME
)
public class MessageInternalDataAccess extends InternalDataAccess<MessageKey, MessageData> implements MessageDataAccess<MessageData> {

    @Override
    protected List<Object> extractIndexedData(MessageData data) {
        return asList(data.getCustomerKey());
    }

    @Override
    public List<MessageData> findByCustomer(CustomerKey customerKey) {
        return findBy(customerKey);
    }

}
