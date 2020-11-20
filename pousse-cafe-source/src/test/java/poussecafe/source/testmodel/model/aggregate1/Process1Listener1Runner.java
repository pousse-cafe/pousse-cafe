package poussecafe.source.testmodel.model.aggregate1;

import poussecafe.listeners.UpdateOneRunner;
import poussecafe.source.testmodel.model.events.Event1;

public class Process1Listener1Runner extends UpdateOneRunner<Event1, String, Aggregate1.Root> {

    @Override
    protected String aggregateId(Event1 message) {
        return null;
    }
}
