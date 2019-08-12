package poussecafe.environment;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import org.junit.Test;
import poussecafe.messaging.Message;

import static org.junit.Assert.assertTrue;

public class MessageListenersPoolTest {

    @Test
    public void splitDoesNotSeparateListenersWithSameLabel() {
        givenMessageListenersPool(42);
        whenSplitting(8);
        thenListenersWithSameLabelAreInSamePool();
        thenLoadEvenlySpread();
    }

    private void givenMessageListenersPool(int collisionSpaces) {
        for(int i = 0; i < collisionSpaces; ++i) {
            String label = "label_" + i;
            buckets.put(label, listeners(label));
        }

        pool = new MessageListenersPool();
        for(Set<MessageListener> bucketListeners : buckets.values()) {
            for(MessageListener listener : bucketListeners) {
                pool.registerListenerForMessageClass(listener, Message.class);
            }
        }
    }

    private Map<String, Set<MessageListener>> buckets = new HashMap<>();

    private Set<MessageListener> listeners(String label) {
        Set<MessageListener> listeners = new HashSet<>();
        for(int i = 0; i < BUCKET_SIZE; ++i) {
            MessageListener listener = new MessageListener.Builder()
                    .id(label + "_" + i)
                    .messageClass(Message.class)
                    .priority(0)
                    .collisionSpace(Optional.of(label))
                    .consumer(message -> {})
                    .build();
            listeners.add(listener);
        }
        return listeners;
    }

    private final static int BUCKET_SIZE = 42;

    private MessageListenersPool pool;

    private void whenSplitting(int partitions) {
        pools = pool.split(partitions);
    }

    private MessageListenersPool[] pools;

    private void thenListenersWithSameLabelAreInSamePool() {
        for(Entry<String, Set<MessageListener>> bucket : buckets.entrySet()) {
            int labelPool = -1;
            for(MessageListener listener : bucket.getValue()) {
                for(int i = 0; i < pools.length; ++i) {
                    MessageListenersPool pool = pools[i];
                    if(pool.contains(listener)) {
                        if(labelPool == -1) {
                            labelPool = i;
                        } else if(labelPool != i) {
                            assertTrue("Label is spread across several pools", false);
                        }
                    }
                }
            }
        }
    }

    private void thenLoadEvenlySpread() {
        int minBucketsPerPool = buckets.size() / pools.length;
        for(int i = 0; i < pools.length; ++i) {
            int bucketsPerPool = pools[i].countListeners() / BUCKET_SIZE;
            assertTrue("Buckets were not evenly distributed", bucketsPerPool == minBucketsPerPool || bucketsPerPool == minBucketsPerPool + 1);
        }
    }

    @Test
    public void splitDoesNotSeparateListenersWithSameLabelIfLessLabelsThanPartitions() {
        givenMessageListenersPool(8);
        whenSplitting(42);
        thenListenersWithSameLabelAreInSamePool();
    }
}
