package poussecafe.source.generation.generatedfull.model.aggregate1;

import poussecafe.listeners.UpdateOneRunner;
import poussecafe.source.generation.generatedfull.model.events.Event4;

public class Process1Listener6Runner extends UpdateOneRunner<Event4, Aggregate1Id, Aggregate1.Root> {

    @Override
    protected Aggregate1Id aggregateId(Event4 message) {
        // TODO: extract id from message
        return null;
    }
}
