package poussecafe.sample;

import java.util.List;
import poussecafe.data.memory.InMemoryDataAccess;
import poussecafe.data.memory.JSONPathSpecification;
import poussecafe.sample.domain.CustomerKey;
import poussecafe.sample.domain.Message;
import poussecafe.sample.domain.Message.Data;
import poussecafe.sample.domain.MessageDataAccess;
import poussecafe.sample.domain.MessageKey;

public class InMemoryMessageDataAccess extends InMemoryDataAccess<MessageKey, Message.Data>
implements MessageDataAccess {

    public InMemoryMessageDataAccess() {
        super(Message.Data.class);
    }

    @Override
    public List<Data> findByCustomer(CustomerKey customerKey) {
        return findByJSONPath(
                new JSONPathSpecification("$.customerKey.id", value -> value.equals(customerKey.getValue())));
    }

}
