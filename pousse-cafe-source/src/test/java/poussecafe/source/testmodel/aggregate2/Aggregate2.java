package poussecafe.source.testmodel.aggregate2;

import poussecafe.discovery.*;
import poussecafe.domain.EntityAttributes;
import poussecafe.source.testmodel.events.Event2;
import poussecafe.source.testmodel.events.Event3;
import poussecafe.source.testmodel.events.Event6;
import poussecafe.source.testmodel.events.Event7;
import poussecafe.source.testmodel.process.Process1;

public class Aggregate2 extends poussecafe.domain.AggregateRoot<String, Aggregate2.Attributes> {

    @MessageListener(processes = {Process1.class}, runner = Process1Listener2Runner.class)
    @ProducesEvent(value = Event3.class, required = false, consumedByExternal = "External2")
    public void process1Listener2(Event2 event) {

    }

    @Override
    @ProducesEvent(Event6.class)
    public void onDelete() {

    }
    
    @Override
    @ProducesEvent(Event7.class)
    public void onUpdate() {

    }

    public static interface Attributes extends EntityAttributes<String> {

    }
}
