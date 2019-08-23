package poussecafe.processing;

import java.util.ArrayList;
import java.util.List;
import poussecafe.runtime.MessageConsumptionHandler;

public class MessageProcessingThreadPool {

    public static class Builder {

        private MessageProcessingThreadPool pool = new MessageProcessingThreadPool();

        public Builder listenersSet(ListenersSet listenersSet) {
            this.listenersSet = listenersSet;
            return this;
        }

        private ListenersSet listenersSet;

        public Builder numberOfThreads(int numberOfThreads) {
            this.numberOfThreads = numberOfThreads;
            return this;
        }

        private int numberOfThreads;

        public Builder failFast(boolean failFast) {
            this.failFast = failFast;
            return this;
        }

        private boolean failFast;

        public Builder messageConsumptionHandler(MessageConsumptionHandler messageConsumptionHandler) {
            this.messageConsumptionHandler = messageConsumptionHandler;
            return this;
        }

        private MessageConsumptionHandler messageConsumptionHandler;

        public MessageProcessingThreadPool build() {
            createThreads();
            if(numberOfThreads <= 0) {
                throw new IllegalStateException("Cannot create empty pool");
            }
            return pool;
        }

        private void createThreads() {
            ListenersSetPartition[] partitions = listenersSet.split(numberOfThreads);
            for(int i = 0; i < numberOfThreads; ++i) {
                ListenersSetPartition listenersPartition = partitions[i];
                MessageProcessor processor = new MessageProcessor.Builder()
                        .id(Integer.toString(i))
                        .failFast(failFast)
                        .messageConsumptionHandler(messageConsumptionHandler)
                        .listenersPartition(listenersPartition)
                        .build();
                MessageProcessingThread thread = new MessageProcessingThread.Builder()
                        .threadId(i)
                        .messageProcessor(processor)
                        .build();
                pool.messageProcessingThreads.add(thread);
            }
        }
    }

    private MessageProcessingThreadPool() {

    }

    private List<MessageProcessingThread> messageProcessingThreads = new ArrayList<>();

    public void start() {
        messageProcessingThreads.forEach(MessageProcessingThread::start);
    }

    public void stop() {
        messageProcessingThreads.forEach(MessageProcessingThread::stop);
    }

    public int size() {
        return messageProcessingThreads.size();
    }

    public boolean isEmpty() {
        return messageProcessingThreads.isEmpty();
    }

    public void submit(MessageToProcess messageToProcess) {
        for(int index = 0; index < messageProcessingThreads.size(); ++index) {
            MessageProcessingThread thread = messageProcessingThreads.get(index);
            thread.submit(messageToProcess);
        }
    }
}
