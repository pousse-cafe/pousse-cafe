package poussecafe.testmodule2;

import java.util.List;
import poussecafe.attribute.Attribute;
import poussecafe.discovery.Aggregate;
import poussecafe.discovery.MessageListener;
import poussecafe.domain.AggregateFactory;
import poussecafe.domain.AggregateRepository;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.EntityAttributes;
import poussecafe.domain.EntityDataAccess;

@Aggregate
public class SimpleAggregate {

    public static class Factory extends AggregateFactory<SimpleAggregateId, Root, Root.Attributes> {

        @MessageListener
        public Root newSimpleAggregate(CreateSimpleAggregate command) {
            var aggregate = newAggregateWithId(command.identifier().value());
            aggregate.attributes().data().valueOf(command.data());
            return aggregate;
        }
    }

    public static class Root extends AggregateRoot<SimpleAggregateId, Root.Attributes> {

        @MessageListener(runner = SimpleAggregateTouchRunner.class)
        public void touch(TestDomainEvent3 event) {
            attributes().data().value("touched");
        }

        public static interface Attributes extends EntityAttributes<SimpleAggregateId> {

            Attribute<String> data();
        }
    }

    public static class Repository extends AggregateRepository<SimpleAggregateId, Root, Root.Attributes> {

        public List<Root> findByData(String data) {
            return wrap(dataAccess().findByData(data));
        }

        @Override
        public DataAccess<Root.Attributes> dataAccess() {
            return (DataAccess<Root.Attributes>) super.dataAccess();
        }

        public static interface DataAccess<D extends Root.Attributes> extends EntityDataAccess<SimpleAggregateId, D> {

            List<D> findByData(String data);
        }
    }
}
