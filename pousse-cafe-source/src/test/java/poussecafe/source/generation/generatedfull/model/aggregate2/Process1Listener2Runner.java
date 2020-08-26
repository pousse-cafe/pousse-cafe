package poussecafe.source.generation.generatedfull.model.aggregate2;

import poussecafe.listeners.UpdateOneRunner;
import poussecafe.source.testmodel.model.events.Event2;

public class Process1Listener2Runner extends UpdateOneRunner<Event2, String, Aggregate2> {

    @Override
    protected String aggregateId(Event2 message) {
        return null;
    }
}
