package poussecafe.source.generation.generatedfull.model.aggregate2;

import poussecafe.listeners.UpdateOneRunner;
import poussecafe.source.generation.generatedfull.model.events.Event2;

public class Process1Listener2Runner extends UpdateOneRunner<Event2, Aggregate2Id, Aggregate2Root> {

    @Override
    protected Aggregate2Id aggregateId(Event2 message) {
        // TODO: extract id from message
        return null;
    }
}
