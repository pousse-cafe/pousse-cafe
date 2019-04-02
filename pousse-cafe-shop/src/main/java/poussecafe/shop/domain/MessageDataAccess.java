package poussecafe.shop.domain;

import java.util.List;
import poussecafe.domain.EntityDataAccess;

public interface MessageDataAccess<N extends Message.Attributes> extends EntityDataAccess<MessageId, N> {

    List<N> findByCustomer(CustomerId customerId);

}
