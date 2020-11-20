package poussecafe.testmodule2;

import java.util.List;
import poussecafe.attribute.Attribute;
import poussecafe.discovery.Aggregate;
import poussecafe.discovery.MessageListener;
import poussecafe.domain.AggregateFactory;
import poussecafe.domain.AggregateRepository;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.EntityAttributes;

@Aggregate
public class SimpleAggregate {

    public static class SimpleAggregateFactory
    extends AggregateFactory<SimpleAggregateId, SimpleAggregateRoot, SimpleAggregateRoot.Attributes> {

        @MessageListener
        public SimpleAggregateRoot newSimpleAggregate(CreateSimpleAggregate command) {
            var aggregate = newAggregateWithId(command.identifier().value());
            aggregate.attributes().data().valueOf(command.data());
            return aggregate;
        }
    }

    public static class SimpleAggregateRoot extends AggregateRoot<SimpleAggregateId, SimpleAggregateRoot.Attributes> {

        @MessageListener(runner = SimpleAggregateTouchRunner.class)
        public void touch(TestDomainEvent3 event) {
            attributes().data().value("touched");
        }

        public static interface Attributes extends EntityAttributes<SimpleAggregateId> {

            Attribute<String> data();
        }
    }

    public static class SimpleAggregateRepository
    extends AggregateRepository<SimpleAggregateId, SimpleAggregateRoot, SimpleAggregateRoot.Attributes> {

        public List<SimpleAggregateRoot> findByData(String data) {
            return wrap(dataAccess().findByData(data));
        }

        @Override
        public SimpleAggregateDataAccess<SimpleAggregateRoot.Attributes> dataAccess() {
            return (SimpleAggregateDataAccess<SimpleAggregateRoot.Attributes>) super.dataAccess();
        }
    }
}
