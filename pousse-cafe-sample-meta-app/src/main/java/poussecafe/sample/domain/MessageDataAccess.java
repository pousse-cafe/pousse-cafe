package poussecafe.sample.domain;

import java.util.List;
import poussecafe.storable.IdentifiedStorableDataAccess;

public interface MessageDataAccess<N extends Message.Data> extends IdentifiedStorableDataAccess<MessageKey, N> {

    List<N> findByCustomer(CustomerKey customerKey);

}
