package poussecafe.sample.adapters.storage;

import java.util.List;
import poussecafe.sample.domain.CustomerKey;
import poussecafe.sample.domain.MessageDataAccess;
import poussecafe.sample.domain.MessageKey;
import poussecafe.storage.internal.InternalDataAccess;

import static java.util.Arrays.asList;

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
