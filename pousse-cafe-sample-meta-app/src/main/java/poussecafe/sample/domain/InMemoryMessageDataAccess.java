package poussecafe.sample.domain;

import java.util.List;
import poussecafe.inmemory.InMemoryDataAccess;
import poussecafe.sample.domain.Message.Data;

import static java.util.Arrays.asList;

public class InMemoryMessageDataAccess extends InMemoryDataAccess<MessageKey, Message.Data> implements MessageDataAccess {

    @Override
    protected List<Object> extractIndexedData(Data data) {
        return asList(data.getCustomerKey());
    }

    @Override
    public List<Message.Data> findByCustomer(CustomerKey customerKey) {
        return findBy(customerKey);
    }

}
