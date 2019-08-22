package poussecafe.processing;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import poussecafe.processing.MessageToProcess.Callback;
import poussecafe.runtime.FailFastException;

public class MessageBroker {

    public MessageBroker(MessageProcessingThreadPool messageProcessingThreadsPool) {
        setThreadPool(messageProcessingThreadsPool);
    }

    private void setThreadPool(MessageProcessingThreadPool messageProcessingThreadsPool) {
        Objects.requireNonNull(messageProcessingThreadsPool);
        if(messageProcessingThreadsPool.isEmpty()) {
            throw new IllegalArgumentException("Cannot instantiate broker with an empty pool");
        }
        this.messageProcessingThreadsPool = messageProcessingThreadsPool;
    }

    private MessageProcessingThreadPool messageProcessingThreadsPool;

    public synchronized void dispatch(ReceivedMessage receivedMessage) {
        if(failFast) {
            throw new FailFastException();
        } else {
            var receivedMessageId = nextReceivedMessageId++;
            ReceivedMessageProcessingState processingState = new ReceivedMessageProcessingState.Builder()
                    .numberOfThreads(messageProcessingThreadsPool.size())
                    .receivedMessage(receivedMessage)
                    .build();
            inProgressProcessingStates.put(receivedMessageId, processingState);
            MessageToProcess messageToProcess = new MessageToProcess.Builder()
                    .receivedMessageId(receivedMessageId)
                    .receivedMessagePayload(receivedMessage.message())
                    .callback(callback)
                    .build();
            messageProcessingThreadsPool.submit(messageToProcess);
        }
    }

    private MessageBrokerCallback callback = new MessageBrokerCallback();

    private boolean failFast;

    private long nextReceivedMessageId = 0;

    public synchronized void replaceThreadPool(MessageProcessingThreadPool newThreadPool) {
        messageProcessingThreadsPool.stop();
        messageProcessingThreadsPool = newThreadPool;
    }

    private synchronized void signalProcessed(int threadId, MessageToProcess processedMessage) { // NOSONAR - synchronized
        var processingProgress = inProgressProcessingStates.get(processedMessage.receivedMessageId());
        if(processingProgress == null) {
            throw new IllegalArgumentException("No processing state available");
        } else {
            processingProgress.ackThreadProcessed(threadId);
            if(processingProgress.isCompleted()) {
                processingProgress.receivedMessage().ack();
                inProgressProcessingStates.remove(processedMessage.receivedMessageId());
            }
        }
    }

    private Map<Long, ReceivedMessageProcessingState> inProgressProcessingStates = new HashMap<>();

    private synchronized void failFast(MessageToProcess processedMessage) { // NOSONAR - synchronized
        failFast = true;
        var processingProgress = inProgressProcessingStates.get(processedMessage.receivedMessageId());
        if(processingProgress == null) {
            throw new IllegalArgumentException("No processing state available");
        } else {
            messageProcessingThreadsPool.stop();
            processingProgress.receivedMessage().interrupt();
        }
    }

    private class MessageBrokerCallback implements Callback {

        @Override
        public void signalProcessed(int threadId, MessageToProcess processedMessage) {
            MessageBroker.this.signalProcessed(threadId, processedMessage);
        }

        @Override
        public void failFast(MessageToProcess processedMessage) {
            MessageBroker.this.failFast(processedMessage);
        }
    }
}
