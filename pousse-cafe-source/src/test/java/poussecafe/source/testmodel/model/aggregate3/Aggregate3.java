package poussecafe.source.testmodel.model.aggregate3;

import poussecafe.discovery.Aggregate;
import poussecafe.discovery.MessageListener;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.EntityAttributes;
import poussecafe.domain.Factory;
import poussecafe.source.testmodel.model.events.Event2;
import poussecafe.source.testmodel.process.Process2;

@Aggregate
public class Aggregate3 {

    public static class Aggregate3Factory extends Factory<String, Aggregate3Root, Aggregate3Root.Attributes> {

        @MessageListener(processes = Process2.class)
        public Aggregate3 process2Listener0(Event2 command) {
            return null;
        }
    }

    public static class Aggregate3Root extends AggregateRoot<String, Aggregate3Root.Attributes> {

        public static interface Attributes extends EntityAttributes<String> {

        }
    }
}
