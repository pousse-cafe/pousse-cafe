package poussecafe.source.testmodel.model.aggregate1;

import java.util.List;
import java.util.Optional;
import poussecafe.discovery.Aggregate;
import poussecafe.discovery.MessageListener;
import poussecafe.discovery.ProducesEvent;
import poussecafe.domain.AggregateFactory;
import poussecafe.domain.AggregateRepository;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.EntityAttributes;
import poussecafe.source.testmodel.commands.Command1;
import poussecafe.source.testmodel.commands.Command3;
import poussecafe.source.testmodel.commands.Command4;
import poussecafe.source.testmodel.model.events.Event1;
import poussecafe.source.testmodel.model.events.Event2;
import poussecafe.source.testmodel.model.events.Event4;
import poussecafe.source.testmodel.model.events.Event5;
import poussecafe.source.testmodel.process.Process1;

@Aggregate
public class Aggregate1 {

    public static class Root extends AggregateRoot<String, Root.Attributes> {

        @Override
        @ProducesEvent(Event5.class)
        public void onAdd() {

        }

        static class SomeInnerClass {

        }

        @MessageListener(processes = Process1.class, runner = Process1Listener1Runner.class, consumesFromExternal = "External1")
        @ProducesEvent(Event2.class)
        public void process1Listener1(Event1 event) {

        }

        @MessageListener(processes = Process1.class, runner = Process1Listener6Runner.class)
        public void process1Listener6(Event4 event) {

        }

        public static interface Attributes extends EntityAttributes<String> {

        }
    }

    public class Factory extends AggregateFactory<String, Root, Root.Attributes> {

        @MessageListener(processes = Process1.class)
        public Aggregate1 process1Listener0(Command1 command) {
            return null;
        }

        @MessageListener(processes = Process1.class)
        public Optional<Aggregate1> process1Listener4(Command3 command) {
            return null;
        }

        @MessageListener(processes = Process1.class)
        public List<Aggregate1> process1Listener5(Command4 command) {
            return null;
        }
    }

    public class Repository extends AggregateRepository<String, Root, Root.Attributes> {

    }
}
