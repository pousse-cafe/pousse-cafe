package poussecafe.testmodule;

import java.util.List;
import poussecafe.discovery.MessageListener;
import poussecafe.domain.AggregateRepository;

import static org.apache.commons.lang3.StringUtils.wrap;

public class SimpleAggregateRepository
extends AggregateRepository<SimpleAggregate, SimpleAggregateId, SimpleAggregate.Attributes> {

    @MessageListener
    public void delete(TestDomainEvent4 event) {
        delete(event.identifier().value());
    }

    public List<SimpleAggregate> findByData(String data) {
        return wrap(dataAccess().findByData(data));
    }

    @Override
    public SimpleAggregateDataAccess<SimpleAggregate.Attributes> dataAccess() {
        return (SimpleAggregateDataAccess<SimpleAggregate.Attributes>) super.dataAccess();
    }
}
