package poussecafe.source.validation.listener;

import poussecafe.listeners.UpdateOneRunner;

public class WrongRunner extends UpdateOneRunner<Message3, String, AggregateRootWithWrongRunner> {

    @Override
    protected String aggregateId(Message3 message) {
        return null;
    }
}
