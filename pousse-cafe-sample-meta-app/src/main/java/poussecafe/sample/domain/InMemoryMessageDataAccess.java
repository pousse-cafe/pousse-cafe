package poussecafe.sample.domain;

import java.util.List;
import poussecafe.inmemory.InMemoryDataAccess;

public class InMemoryMessageDataAccess extends InMemoryDataAccess<Message.Data> implements MessageDataAccess {

    @Override
    public List<Message.Data> findByCustomer(CustomerKey customerKey) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected Object extractKey(Message.Data data) {
        return data.key().get();
    }

}
