package poussecafe.processing;

import org.junit.Test;
import poussecafe.environment.MessageConsumptionReport;
import poussecafe.environment.MessageListener;
import poussecafe.environment.MessageListenerType;
import poussecafe.environment.MessageListenersPool;
import poussecafe.messaging.Message;

import static org.junit.Assert.assertTrue;

public class ListenersSetTest {

    @Test
    public void splitProducesActualPartitions() {
        givenListenersSet();
        whenSplittingSet();
        thenPartitionsAreActualPartitions();
    }

    private void givenListenersSet() {
        MessageListenersPool messageListenersPool = new MessageListenersPool();
        for(int i = 0; i < 8; ++i) {
            MessageListener listener = new MessageListener.Builder()
                    .id(Integer.toString(i))
                    .shortId(Integer.toString(i))
                    .consumer(message -> MessageConsumptionReport.success())
                    .messageClass(Message.class)
                    .priority(MessageListenerType.CUSTOM)
                    .build();
            messageListenersPool.registerListenerForMessageClass(listener, Message.class);
        }
        listenersSet = new ListenersSet(messageListenersPool);
    }

    private ListenersSet listenersSet;

    private void whenSplittingSet() {
        partitions = listenersSet.split(4);
    }

    private ListenersSetPartition[] partitions;

    private void thenPartitionsAreActualPartitions() {
        for(int i = 0; i < partitions.length; ++i) {
            ListenersSetPartition partition = partitions[i];
            for(MessageListener listener : partition.partitionListenersSet().messageListeners()) {
                assertTrue(listenerOnlyInPartition(listener, i));
            }
        }
    }

    private boolean listenerOnlyInPartition(MessageListener listener, int partitionIndex) {
        for(int i = 0; i < partitions.length; ++i) {
            if(i != partitionIndex) {
                ListenersSetPartition partition = partitions[i];
                if(partition.partitionListenersSet().contains(listener)) {
                    return false;
                }
            }
        }
        return true;
    }
}
