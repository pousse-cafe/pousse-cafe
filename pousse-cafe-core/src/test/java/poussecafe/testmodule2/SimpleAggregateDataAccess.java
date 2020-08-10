package poussecafe.testmodule2;

import java.util.List;
import poussecafe.domain.EntityDataAccess;
import poussecafe.testmodule2.SimpleAggregate.SimpleAggregateRoot;

public interface SimpleAggregateDataAccess<D extends SimpleAggregateRoot.Attributes>
extends EntityDataAccess<SimpleAggregateId, D> {

    List<D> findByData(String data);
}
