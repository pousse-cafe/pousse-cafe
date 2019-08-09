package poussecafe.processing;

import java.util.Collection;
import java.util.Objects;
import poussecafe.environment.MessageListener;
import poussecafe.environment.MessageListenersPool;
import poussecafe.messaging.Message;

public class ListenersSet {

    public static class Builder {

        private ListenersSet listenersSet = new ListenersSet();

        public Builder messageListenersPool(MessageListenersPool messageListenersPool) {
            listenersSet.messageListenersPool = messageListenersPool;
            return this;
        }

        public ListenersSet build() {
            Objects.requireNonNull(listenersSet.messageListenersPool);
            return listenersSet;
        }
    }

    private ListenersSet() {

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
            ListenersSet listenersSet = new ListenersSet.Builder()
                    .messageListenersPool(pools[i])
                    .build();
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
