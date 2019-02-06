package poussecafe.sample.domain;

import java.util.List;
import poussecafe.domain.EntityDataAccess;

public interface MessageDataAccess<N extends Message.Attributes> extends EntityDataAccess<MessageKey, N> {

    List<N> findByCustomer(CustomerKey customerKey);

}
