package poussecafe.domain.chain2;

import poussecafe.domain.chain1.ChainElementTouchable;
import poussecafe.environment.AggregateMessageListenerRunner;
import poussecafe.environment.TargetAggregates;
import poussecafe.messaging.internal.NextChainElementId;

public class Chain2ElementTouchRunner
implements AggregateMessageListenerRunner<ChainElementTouchable, Chain2ElementId, Chain2Element> {

    @Override
    public TargetAggregates<Chain2ElementId> targetAggregates(ChainElementTouchable event) {
        NextChainElementId elementId = event.next().value();
        TargetAggregates.Builder<Chain2ElementId> builder = new TargetAggregates.Builder<>();
        if(elementId.typeNumber() == 1) {
            builder.toUpdate(elementId.toChain2ElementId());
        }
        return builder.build();
    }
}
