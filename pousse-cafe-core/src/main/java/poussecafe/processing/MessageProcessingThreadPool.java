package poussecafe.processing;

public class MessageProcessingThreadPool {

    public static class Builder {

        private MessageProcessingThreadPool pool = new MessageProcessingThreadPool();

        public Builder poolSize(int poolSize) {
            this.poolSize = poolSize;
            return this;
        }

        private int poolSize;

        public Builder messageConsumptionConfiguration(MessageConsumptionConfiguration messageConsumptionConfiguration) {
            this.messageConsumptionConfiguration = messageConsumptionConfiguration;
            return this;
        }

        private MessageConsumptionConfiguration messageConsumptionConfiguration;

        public MessageProcessingThreadPool build() {
            if(poolSize <= 0) {
                throw new IllegalStateException("Message processing thread pool size must be greater than zero");
            }
            pool.messageProcessingThreads = new MessageProcessingThread[poolSize];
            createThreads();
            return pool;
        }

        private void createThreads() {
            for(int i = 0; i < pool.messageProcessingThreads.length; ++i) {
                MessageProcessor processor = new MessageProcessor.Builder()
                        .id(Integer.toString(i))
                        .messageConsumptionConfiguration(messageConsumptionConfiguration)
                        .build();
                MessageProcessingThread thread = new MessageProcessingThread.Builder()
                        .threadId(i)
                        .messageProcessor(processor)
                        .build();
                pool.messageProcessingThreads[i] = thread;
            }
        }
    }

    private MessageProcessingThreadPool() {

    }

    private MessageProcessingThread[] messageProcessingThreads;

    public void start() {
        for(int i = 0; i < messageProcessingThreads.length; ++i) {
            messageProcessingThreads[i].start();
        }
    }

    public void stop() {
        for(int i = 0; i < messageProcessingThreads.length; ++i) {
            messageProcessingThreads[i].stop();
        }
    }

    public int size() {
        return messageProcessingThreads.length;
    }

    public void submit(int threadId, MessageToProcess messageToProcess) {
        if(threadId < 0 || threadId >= messageProcessingThreads.length) {
            throw new IllegalArgumentException("Thread ID must be between 0 and (pool size - 1) inclusive");
        }
        MessageProcessingThread thread = messageProcessingThreads[threadId];
        thread.submit(messageToProcess);
    }
}
