package poussecafe.source.generation.generatedfull.model.aggregate1;

import poussecafe.listeners.UpdateOneRunner;
import poussecafe.source.generation.generatedfull.model.events.Event1;

public class Process1Listener1Runner extends UpdateOneRunner<Event1, Aggregate1Id, Aggregate1> {

    @Override
    protected Aggregate1Id aggregateId(Event1 message) {
        // TODO: extract id from message
        return null;
    }
}
