package poussecafe.source.testmodel.model.aggregate3;

import poussecafe.discovery.Aggregate;
import poussecafe.discovery.MessageListener;
import poussecafe.domain.AggregateFactory;
import poussecafe.domain.AggregateRepository;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.EntityAttributes;
import poussecafe.source.testmodel.model.events.Event2;
import poussecafe.source.testmodel.process.Process2;

@Aggregate
public class Aggregate3 {

    public static class Factory extends AggregateFactory<String, Root, Root.Attributes> {

        @MessageListener(processes = Process2.class)
        public Aggregate3 process2Listener0(Event2 command) {
            return null;
        }
    }

    public static class Root extends AggregateRoot<String, Root.Attributes> {

        public static interface Attributes extends EntityAttributes<String> {

        }
    }

    public static class Repository extends AggregateRepository<String, Root, Root.Attributes> {

    }
}
