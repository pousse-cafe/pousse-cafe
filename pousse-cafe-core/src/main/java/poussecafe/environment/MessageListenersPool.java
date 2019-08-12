package poussecafe.environment;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import org.slf4j.Logger;
import poussecafe.messaging.Message;

import static java.util.Collections.emptySet;
import static org.slf4j.LoggerFactory.getLogger;

public class MessageListenersPool {

    public void registerListenerForMessageClass(MessageListener listener, Class<? extends Message> messageClass) {
        Set<MessageListener> messageClassListeners = listenersByMessageClass.computeIfAbsent(messageClass, key -> new HashSet<>());
        if(!messageClassListeners.add(listener)) {
            throw new IllegalArgumentException("Listener could not be registered for message " + messageClass.getName() + ", would hide another one: " + listener);
        }

        Set<Class<? extends Message>> messageClasses = messageClassesByListener.computeIfAbsent(listener, key -> new HashSet<>());
        if(!messageClasses.add(messageClass)) {
            throw new IllegalArgumentException("Listener " + listener + " already linked to message " + messageClass);
        }
    }

    private Map<Class<? extends Message>, Set<MessageListener>> listenersByMessageClass = new HashMap<>();

    public Set<MessageListener> getListeners(Class<? extends Message> messageClass) {
        return getListenersForMessageClass(messageClass);
    }

    private Set<MessageListener> getListenersForMessageClass(Class<? extends Message> key) {
        return Optional.ofNullable(listenersByMessageClass.get(key)).map(Collections::unmodifiableSet).orElse(emptySet());
    }

    public Collection<MessageListener> allListeners() {
        return Collections.unmodifiableSet(messageClassesByListener.keySet());
    }

    public MessageListenersPool[] split(int expectedNumberOfPartitions) {
        if(expectedNumberOfPartitions < 1) {
            throw new IllegalArgumentException("Number of partitions must be greater than 0");
        }

        Map<String, Set<MessageListener>> buckets = buildBuckets();
        logBuckets(buckets);

        MessageListenersPool[] pools = buildEmptyPools(expectedNumberOfPartitions);
        for(Entry<String, Set<MessageListener>> bucket : buckets.entrySet()) {
            int poolId = findLeastLoadedPool(pools);
            MessageListenersPool pool = pools[poolId];
            logger.debug("Assigning bucket {} to pool {}", bucket.getKey(), poolId);
            registerBucket(pool, bucket);
        }
        return pools;
    }

    private Logger logger = getLogger(getClass());

    private Map<String, Set<MessageListener>> buildBuckets() {
        Map<String, Set<MessageListener>> buckets = new HashMap<>();
        for(MessageListener listener : messageClassesByListener.keySet()) {
            Set<MessageListener> bucket =
                    buckets.computeIfAbsent(listener.collisionSpace().orElse("default"), key -> new HashSet<>());
            bucket.add(listener);
        }
        return buckets;
    }

    private Map<MessageListener, Set<Class<? extends Message>>> messageClassesByListener = new HashMap<>();

    private void logBuckets(Map<String, Set<MessageListener>> buckets) {
        logger.debug("Detected {} buckets", buckets.size());
        if(logger.isDebugEnabled()) {
            for(Entry<String, Set<MessageListener>> bucket : buckets.entrySet()) {
                logger.debug("    {}: {} listeners", bucket.getKey(), bucket.getValue().size());
            }
        }
    }

    private MessageListenersPool[] buildEmptyPools(int expectedNumberOfPartitions) {
        MessageListenersPool[] pools = new MessageListenersPool[expectedNumberOfPartitions];
        for(int i = 0; i < pools.length; ++i) {
            pools[i] = new MessageListenersPool();
        }
        return pools;
    }

    private int findLeastLoadedPool(MessageListenersPool[] pools) {
        int leastLoadedPool = 0;
        int lowestLoad = pools[0].messageClassesByListener.size();
        for(int i = 1; i < pools.length; ++i) {
            MessageListenersPool candidatePool = pools[i];
            int candidateLoad = candidatePool.messageClassesByListener.size();
            if(candidateLoad < lowestLoad) {
                leastLoadedPool = i;
                lowestLoad = candidateLoad;
            }
        }
        return leastLoadedPool;
    }

    private void registerBucket(MessageListenersPool pool, Entry<String, Set<MessageListener>> bucket) {
        for(MessageListener listener : bucket.getValue()) {
            Set<Class<? extends Message>> listenerMessages = messageClassesByListener.get(listener);
            for(Class<? extends Message> listenerMessage : listenerMessages) {
                pool.registerListenerForMessageClass(listener, listenerMessage);
            }
        }
    }

    public boolean contains(MessageListener listener) {
        return messageClassesByListener.keySet().contains(listener);
    }

    public int countListeners() {
        return messageClassesByListener.size();
    }
}
