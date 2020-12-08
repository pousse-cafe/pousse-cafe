package poussecafe.source.generation.generatedfull.model.aggregate1;

import java.util.Collection;
import java.util.Optional;
import poussecafe.discovery.Aggregate;
import poussecafe.discovery.MessageListener;
import poussecafe.discovery.ProducesEvent;
import poussecafe.domain.AggregateFactory;
import poussecafe.domain.AggregateRepository;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.EntityAttributes;
import poussecafe.source.generation.generatedfull.commands.Command1;
import poussecafe.source.generation.generatedfull.commands.Command3;
import poussecafe.source.generation.generatedfull.commands.Command4;
import poussecafe.source.generation.generatedfull.model.events.Event1;
import poussecafe.source.generation.generatedfull.model.events.Event2;
import poussecafe.source.generation.generatedfull.model.events.Event4;
import poussecafe.source.generation.generatedfull.model.events.Event5;
import poussecafe.source.generation.generatedfull.process.Process1;

@Aggregate
public class Aggregate1 {

    public static class Factory extends AggregateFactory<Aggregate1Id, Root, Root.Attributes> {

        @MessageListener(processes = Process1.class)
        public Root process1Listener0(Command1 command) {
            // TODO: build aggregate
            return null;
        }

        @MessageListener(processes = Process1.class)
        public Optional<Root> process1Listener4(Command3 command) {
            // TODO: build optional aggregate
            return null;
        }

        @MessageListener(processes = Process1.class)
        public Collection<Root> process1Listener5(Command4 command) {
            // TODO: build aggregate(s)
            return null;
        }
    }

    public static class Root extends AggregateRoot<Aggregate1Id, Root.Attributes> {

        @Override
        @ProducesEvent(Event5.class)
        public void onAdd() {
            // TODO: issue expected event(s)
        }

        @MessageListener(
            processes = Process1.class,
            runner = Process1Listener1Runner.class,
            consumesFromExternal = "External1"
        )
        @ProducesEvent(Event2.class)
        public void process1Listener1(Event1 event) {
            // TODO: update attributes and issue expected event(s)
        }

        @MessageListener(processes = Process1.class, runner = Process1Listener6Runner.class)
        public void process1Listener6(Event4 event) {
            // TODO: update attributes
        }

        public static interface Attributes extends EntityAttributes<Aggregate1Id> {
        }
    }

    public static class Repository extends AggregateRepository<Aggregate1Id, Root, Root.Attributes> {

        @Override
        public Aggregate1DataAccess<Root.Attributes> dataAccess() {
            return (Aggregate1DataAccess<Root.Attributes>) super.dataAccess();
        }
    }

    private Aggregate1() {
    }
}