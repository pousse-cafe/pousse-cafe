package poussecafe.testmodule;

import java.util.List;
import poussecafe.discovery.DataAccessImplementation;
import poussecafe.storage.internal.InternalDataAccess;
import poussecafe.storage.internal.InternalStorage;

import static java.util.stream.Collectors.toList;

@DataAccessImplementation(
    aggregateRoot = SimpleAggregate.class,
    dataImplementation = SimpleAggregateData.class,
    storageName = InternalStorage.NAME
)
public class InternalSimpleAggregateDataAccess
extends InternalDataAccess<SimpleAggregateId, SimpleAggregateData>
implements SimpleAggregateDataAccess<SimpleAggregateData> {

    @Override
    public List<SimpleAggregateData> findByData(String data) {
        return findAll().stream()
                .filter(aggregateData -> aggregateData.data().value().equals(data))
                .collect(toList());
    }
}
