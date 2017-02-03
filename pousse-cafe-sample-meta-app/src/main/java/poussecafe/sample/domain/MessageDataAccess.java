package poussecafe.sample.domain;

import java.util.List;
import poussecafe.sample.domain.Message.Data;
import poussecafe.storable.StorableDataAccess;

public interface MessageDataAccess extends StorableDataAccess<MessageKey, Message.Data> {

    List<Data> findByCustomer(CustomerKey customerKey);

}
