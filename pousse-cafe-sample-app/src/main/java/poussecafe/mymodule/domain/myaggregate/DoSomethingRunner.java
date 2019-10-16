package poussecafe.mymodule.domain.myaggregate;

import poussecafe.discovery.SingleAggregateMessageListenerRunner;
import poussecafe.mymodule.ACommand;

public class DoSomethingRunner extends SingleAggregateMessageListenerRunner<ACommand, MyAggregateId, MyAggregate> {

    @Override
    public MyAggregateId targetAggregateId(ACommand message) {
        return message.id().value();
    }
}
