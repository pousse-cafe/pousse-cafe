package poussecafe.test.domain.chain1;

import poussecafe.environment.AggregateMessageListenerRunner;
import poussecafe.environment.TargetAggregates;
import poussecafe.messaging.internal.NextChainElementId;

public class Chain1ElementTouchRunner
implements AggregateMessageListenerRunner<ChainElementTouchable, Chain1ElementId, Chain1Element> {

    @Override
    public TargetAggregates<Chain1ElementId> targetAggregates(ChainElementTouchable event) {
        NextChainElementId elementId = event.next().value();
        TargetAggregates.Builder<Chain1ElementId> builder = new TargetAggregates.Builder<>();
        if(elementId.typeNumber() == 1) {
            builder.toUpdate(elementId.toChain1ElementId());
        }
        return builder.build();
    }
}
