package poussecafe.sample.domain;

import java.util.List;
import poussecafe.sample.domain.Message.Data;
import poussecafe.storable.IdentifiedStorableDataAccess;

public interface MessageDataAccess extends IdentifiedStorableDataAccess<MessageKey, Message.Data> {

    List<Data> findByCustomer(CustomerKey customerKey);

}
