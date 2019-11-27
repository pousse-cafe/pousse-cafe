package poussecafe.test;

import poussecafe.environment.MessageListenersPool;
import poussecafe.environment.MessageListenersPoolSplitStrategy;


public class ReplicationStrategy implements MessageListenersPoolSplitStrategy {

    public ReplicationStrategy(int numberOfPools) {
        this.numberOfPools = numberOfPools;
    }

    private int numberOfPools;

    @Override
    public MessageListenersPool[] split(MessageListenersPool pool) {
        MessageListenersPool[] pools = new MessageListenersPool[numberOfPools];
        for(int i = 0; i < pools.length; ++i) {
            pools[i] = pool;
        }
        return pools;
    }
}
