package poussecafe.test.domain.chain2;

import poussecafe.environment.AggregateMessageListenerRunner;
import poussecafe.environment.TargetAggregates;
import poussecafe.messaging.internal.NextChainElementId;
import poussecafe.test.domain.chain1.ChainElementTouchable;

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
