package poussecafe.domain.chain2;

import java.util.Set;
import poussecafe.discovery.DefaultAggregateMessageListenerRunner;
import poussecafe.domain.chain1.ChainElementTouchable;
import poussecafe.messaging.internal.NextChainElementId;

import static java.util.Collections.emptySet;
import static poussecafe.collection.Collections.asSet;

public class Chain2ElementTouchRunner extends DefaultAggregateMessageListenerRunner<ChainElementTouchable, Chain2ElementId, Chain2Element> {

    @Override
    public Set<Chain2ElementId> targetAggregatesIds(ChainElementTouchable event) {
        NextChainElementId elementId = event.next().value();
        if(elementId.typeNumber() == 2) {
            return asSet(elementId.toChain2ElementId());
        } else {
            return emptySet();
        }
    }
}
