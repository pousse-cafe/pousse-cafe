package poussecafe.source.testmodel.model.aggregate1;

import poussecafe.listeners.UpdateOneRunner;
import poussecafe.source.testmodel.model.events.Event4;

public class Process1Listener6Runner extends UpdateOneRunner<Event4, String, Aggregate1.Root> {

    @Override
    protected String aggregateId(Event4 message) {
        return null;
    }
}
