package poussecafe.domain.chain3;

import java.util.Set;
import poussecafe.discovery.DefaultAggregateMessageListenerRunner;
import poussecafe.domain.chain1.ChainElementTouchable;
import poussecafe.messaging.internal.NextChainElementId;

import static java.util.Collections.emptySet;
import static poussecafe.collection.Collections.asSet;

public class Chain3ElementTouchRunner extends DefaultAggregateMessageListenerRunner<ChainElementTouchable, Chain3ElementId, Chain3Element> {

    @Override
    public Set<Chain3ElementId> targetAggregatesIds(ChainElementTouchable event) {
        NextChainElementId elementId = event.next().value();
        if(elementId.typeNumber() == 3) {
            return asSet(elementId.toChain3ElementId());
        } else {
            return emptySet();
        }
    }
}
