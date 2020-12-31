package poussecafe.source.validation.listener;

import poussecafe.listeners.UpdateOneRunner;

public class UpdatorRunner extends UpdateOneRunner<Message2, String, MyAggregate.Root> {

    @Override
    protected String aggregateId(Message2 message) {
        return null;
    }
}
