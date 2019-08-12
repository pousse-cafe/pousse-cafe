package poussecafe.processing;

import java.util.Collection;
import java.util.Objects;
import poussecafe.environment.MessageListener;
import poussecafe.environment.MessageListenersPool;
import poussecafe.messaging.Message;

public class ListenersSet {

    public ListenersSet(MessageListenersPool messageListenersPool) {
        Objects.requireNonNull(messageListenersPool);
        this.messageListenersPool = messageListenersPool;
    }

    private MessageListenersPool messageListenersPool;

    public Collection<MessageListener> messageListeners() {
        return messageListenersPool.allListeners();
    }

    public Collection<MessageListener> messageListenersOf(Class<? extends Message> messageClass) {
        return messageListenersPool.getListeners(messageClass);
    }

    ListenersSetPartition[] split(int expectedNumberOfPartitions) {
        MessageListenersPool[] pools = messageListenersPool.split(expectedNumberOfPartitions);
        ListenersSetPartition[] partitions = new ListenersSetPartition[expectedNumberOfPartitions];
        for(int i = 0; i < partitions.length; ++i) {
            ListenersSet listenersSet = new ListenersSet(pools[i]);
            partitions[i] = new ListenersSetPartition.Builder()
                    .ofSet(this)
                    .partitionListenersSet(listenersSet)
                    .build();
        }
        return partitions;
    }

    public boolean contains(MessageListener listener) {
        return messageListenersPool.contains(listener);
    }
}