package poussecafe.source.testmodel.model.aggregate1;

import poussecafe.discovery.MessageListener;
import poussecafe.discovery.ProducesEvent;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.EntityAttributes;
import poussecafe.source.testmodel.model.events.Event1;
import poussecafe.source.testmodel.model.events.Event2;
import poussecafe.source.testmodel.model.events.Event4;
import poussecafe.source.testmodel.model.events.Event5;
import poussecafe.source.testmodel.process.Process1;

public class Aggregate1 extends AggregateRoot<String, Aggregate1.Attributes> {

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
