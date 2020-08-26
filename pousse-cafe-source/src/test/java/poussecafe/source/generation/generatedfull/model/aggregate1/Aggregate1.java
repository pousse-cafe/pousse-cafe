package poussecafe.source.generation.generatedfull.model.aggregate1;

import poussecafe.discovery.Aggregate;
import poussecafe.discovery.DefaultModule;
import poussecafe.discovery.MessageListener;
import poussecafe.discovery.ProducesEvent;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.EntityAttributes;
import poussecafe.source.generation.generatedfull.model.events.Event1;
import poussecafe.source.generation.generatedfull.model.events.Event2;
import poussecafe.source.generation.generatedfull.model.events.Event4;
import poussecafe.source.generation.generatedfull.model.events.Event5;
import poussecafe.source.generation.generatedfull.process.Process1;

@Aggregate(factory = Aggregate1Factory.class, repository = Aggregate1Repository.class, module = DefaultModule.class)
public class Aggregate1 extends AggregateRoot<Aggregate1Id, Aggregate1.Attributes> {

    @Override
    @ProducesEvent(Event5.class)
    public void onAdd() {
    }

    @MessageListener(processes = Process1.class, runner = Process1Listener1Runner.class, consumesFromExternal = "External1")
    @ProducesEvent(Event2.class)
    public void process1Listener1(Event1 event) {
    }

    @MessageListener(processes = Process1.class, runner = Process1Listener6Runner.class)
    public void process1Listener6(Event4 event) {
    }

    public static interface Attributes extends EntityAttributes<Aggregate1Id> {
    }
}
