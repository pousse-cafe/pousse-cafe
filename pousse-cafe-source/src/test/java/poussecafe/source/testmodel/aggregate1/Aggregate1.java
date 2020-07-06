package poussecafe.source.testmodel.aggregate1;

import poussecafe.discovery.MessageListener;
import poussecafe.discovery.ProducesEvent;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.EntityAttributes;
import poussecafe.source.testmodel.events.Event1;
import poussecafe.source.testmodel.events.Event2;
import poussecafe.source.testmodel.events.Event4;
import poussecafe.source.testmodel.process.Process1;

public class Aggregate1 extends AggregateRoot<String, Aggregate1.Attributes> {

    @Override
    public void onAdd() {

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
