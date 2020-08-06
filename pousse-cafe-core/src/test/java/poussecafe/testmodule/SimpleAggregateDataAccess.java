package poussecafe.testmodule;

import java.util.List;
import poussecafe.domain.EntityDataAccess;

public interface SimpleAggregateDataAccess<D extends SimpleAggregate.Attributes>
extends EntityDataAccess<SimpleAggregateId, D> {

    List<D> findByData(String data);
}
