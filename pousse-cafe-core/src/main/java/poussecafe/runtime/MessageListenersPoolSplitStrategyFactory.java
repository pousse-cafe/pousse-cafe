package poussecafe.runtime;

import poussecafe.environment.CollisionPreventionStrategy;
import poussecafe.environment.MessageListenersPoolSplitStrategy;

public class MessageListenersPoolSplitStrategyFactory {

    public MessageListenersPoolSplitStrategy build(MessageListenersPoolSplitStrategySpecification specification) {
        if(specification.strategyType() == MessageListenersPoolSplitStrategyType.COLLISION_PREVENTION) {
            return new CollisionPreventionStrategy.Builder()
                    .expectedNumberOfPools(specification.expectedNumberOfPools())
                    .build();
        } else {
            throw new IllegalArgumentException("Unsupported strategy type " + specification.strategyType());
        }
    }
}
