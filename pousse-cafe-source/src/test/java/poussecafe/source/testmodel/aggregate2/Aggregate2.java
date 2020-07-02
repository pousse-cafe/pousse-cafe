package poussecafe.source.testmodel.aggregate2;

import poussecafe.discovery.*;
import poussecafe.domain.EntityAttributes;
import poussecafe.source.testmodel.events.Event2;
import poussecafe.source.testmodel.process.Process1;

public class Aggregate2 extends poussecafe.domain.AggregateRoot<String, Aggregate2.Attributes> {

    @MessageListener(processes = Process1.class)
    public void process1Listener2(Event2 event) {

    }

    public static interface Attributes extends EntityAttributes<String> {

    }
}
