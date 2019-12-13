package poussecafe.domain.chain3;

import poussecafe.domain.chain1.ChainElementTouchable;
import poussecafe.environment.TargetAggregates;
import poussecafe.listeners.NoContextByDefaultRunner;
import poussecafe.messaging.internal.NextChainElementId;

public class Chain3ElementTouchRunner
extends NoContextByDefaultRunner<ChainElementTouchable, Chain3ElementId, Chain3Element> {

    @Override
    public TargetAggregates<Chain3ElementId> targetAggregates(ChainElementTouchable event) {
        NextChainElementId elementId = event.next().value();
        TargetAggregates.Builder<Chain3ElementId> builder = new TargetAggregates.Builder<>();
        if(elementId.typeNumber() == 1) {
            builder.toUpdate(elementId.toChain3ElementId());
        }
        return builder.build();
    }
}
