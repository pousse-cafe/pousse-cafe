package poussecafe.environment;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import org.slf4j.Logger;
import poussecafe.messaging.Message;

import static org.slf4j.LoggerFactory.getLogger;

public class CollisionPreventionStrategy implements MessageListenersPoolSplitStrategy {

    public static class Builder {

        private CollisionPreventionStrategy strategy = new CollisionPreventionStrategy();

        public Builder expectedNumberOfPools(int expectedNumberOfPools) {
            this.expectedNumberOfPools = expectedNumberOfPools;
            return this;
        }

        private Integer expectedNumberOfPools;

        public CollisionPreventionStrategy build() {
            Objects.requireNonNull(expectedNumberOfPools);
            strategy.expectedNumberOfPools = expectedNumberOfPools;
            return strategy;
        }
    }

    private CollisionPreventionStrategy() {

    }

    private int expectedNumberOfPools;

    @Override
    public MessageListenersPool[] split(MessageListenersPool pool) {
        if(expectedNumberOfPools < 1) {
            throw new IllegalArgumentException("Number of partitions must be greater than 0");
        }

        Map<String, Set<MessageListener>> buckets = buildBuckets(pool.messageClassesByListener());
        logBuckets(buckets);

        MessageListenersPool[] pools = buildEmptyPools(expectedNumberOfPools);
        for(Entry<String, Set<MessageListener>> bucket : buckets.entrySet()) {
            int poolId = findLeastLoadedPool(pools);
            MessageListenersPool bucketPool = pools[poolId];

            logger.debug("Assigning bucket {} to pool {}", bucket.getKey(), poolId);
            for(MessageListener listener : bucket.getValue()) {
                Set<Class<? extends Message>> listenerMessages = pool.messageClassesByListener().get(listener);
                for(Class<? extends Message> listenerMessage : listenerMessages) {
                    bucketPool.registerListenerForMessageClass(listener, listenerMessage);
                }
            }
        }
        return pools;
    }

    private Map<String, Set<MessageListener>> buildBuckets(Map<MessageListener, Set<Class<? extends Message>>> messageClassesByListener) {
        Map<String, Set<MessageListener>> buckets = new HashMap<>();
        for(MessageListener listener : messageClassesByListener.keySet()) {
            Set<MessageListener> bucket =
                    buckets.computeIfAbsent(listener.collisionSpace().orElse("default"), key -> new HashSet<>());
            bucket.add(listener);
        }
        return buckets;
    }

    private void logBuckets(Map<String, Set<MessageListener>> buckets) {
        logger.debug("Detected {} buckets", buckets.size());
        if(logger.isDebugEnabled()) {
            for(Entry<String, Set<MessageListener>> bucket : buckets.entrySet()) {
                logger.debug("    {}: {} listeners", bucket.getKey(), bucket.getValue().size());
            }
        }
    }

    private Logger logger = getLogger(getClass());

    private MessageListenersPool[] buildEmptyPools(int expectedNumberOfPartitions) {
        MessageListenersPool[] pools = new MessageListenersPool[expectedNumberOfPartitions];
        for(int i = 0; i < pools.length; ++i) {
            pools[i] = new MessageListenersPool();
        }
        return pools;
    }

    private int findLeastLoadedPool(MessageListenersPool[] pools) {
        int leastLoadedPool = 0;
        int lowestLoad = pools[0].countListeners();
        for(int i = 1; i < pools.length; ++i) {
            MessageListenersPool candidatePool = pools[i];
            int candidateLoad = candidatePool.countListeners();
            if(candidateLoad < lowestLoad) {
                leastLoadedPool = i;
                lowestLoad = candidateLoad;
            }
        }
        return leastLoadedPool;
    }
}
