package poussecafe.source.generation.generatedfull.model.aggregate2;

import poussecafe.discovery.Aggregate;
import poussecafe.discovery.DefaultModule;
import poussecafe.discovery.MessageListener;
import poussecafe.discovery.ProducesEvent;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.EntityAttributes;
import poussecafe.source.generation.generatedfull.model.events.Event2;
import poussecafe.source.generation.generatedfull.model.events.Event3;
import poussecafe.source.generation.generatedfull.model.events.Event6;
import poussecafe.source.generation.generatedfull.process.Process1;

@Aggregate(factory = Aggregate2Factory.class, repository = Aggregate2Repository.class, module = DefaultModule.class)
public class Aggregate2 extends AggregateRoot<Aggregate2Id, Aggregate2.Attributes> {

    @Override
    @ProducesEvent(Event6.class)
    public void onDelete() {
    }

    @MessageListener(processes = Process1.class, runner = Process1Listener2Runner.class)
    @ProducesEvent(value = Event3.class, required = false, consumedByExternal = "External2")
    public void process1Listener2(Event2 event) {
    }

    public static interface Attributes extends EntityAttributes<Aggregate2Id> {
    }
}