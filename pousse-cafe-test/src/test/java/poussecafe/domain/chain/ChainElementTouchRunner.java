package poussecafe.domain.chain;

import poussecafe.discovery.SingleAggregateMessageListenerRunner;

public class ChainElementTouchRunner extends SingleAggregateMessageListenerRunner<ChainElementTouchable, ChainElementId, ChainElement> {

    @Override
    public ChainElementId targetAggregateId(ChainElementTouchable event) {
        return event.next().value();
    }
}
