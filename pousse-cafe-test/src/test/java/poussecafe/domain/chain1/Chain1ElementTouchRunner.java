package poussecafe.domain.chain1;

import java.util.Set;
import poussecafe.discovery.DefaultAggregateMessageListenerRunner;
import poussecafe.messaging.internal.NextChainElementId;

import static java.util.Collections.emptySet;
import static poussecafe.collection.Collections.asSet;

public class Chain1ElementTouchRunner extends DefaultAggregateMessageListenerRunner<ChainElementTouchable, Chain1ElementId, Chain1Element> {

    @Override
    public Set<Chain1ElementId> targetAggregatesIds(ChainElementTouchable event) {
        NextChainElementId elementId = event.next().value();
        if(elementId.typeNumber() == 1) {
            return asSet(elementId.toChain1ElementId());
        } else {
            return emptySet();
        }
    }
}
