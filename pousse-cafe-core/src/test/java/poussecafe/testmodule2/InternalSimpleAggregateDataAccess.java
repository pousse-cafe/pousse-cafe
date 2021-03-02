package poussecafe.testmodule2;

import java.util.List;
import poussecafe.discovery.DataAccessImplementation;
import poussecafe.storage.internal.InternalDataAccess;
import poussecafe.storage.internal.InternalStorage;
import poussecafe.testmodule2.SimpleAggregate.Repository;
import poussecafe.testmodule2.SimpleAggregate.Root;

import static java.util.stream.Collectors.toList;

@DataAccessImplementation(
    aggregateRoot = Root.class,
    dataImplementation = SimpleAggregateData.class,
    storageName = InternalStorage.NAME
)
public class InternalSimpleAggregateDataAccess
extends InternalDataAccess<SimpleAggregateId, SimpleAggregateData>
implements Repository.DataAccess<SimpleAggregateData> {

    @Override
    public List<SimpleAggregateData> findByData(String data) {
        return findAll().stream()
                .filter(aggregateData -> aggregateData.data().value().equals(data))
                .collect(toList());
    }
}
