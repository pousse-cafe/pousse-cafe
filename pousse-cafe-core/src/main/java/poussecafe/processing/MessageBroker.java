package poussecafe.processing;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MessageBroker {

    public MessageBroker(MessageProcessingThreadPool messageProcessingThreadsPool) {
        Objects.requireNonNull(messageProcessingThreadsPool);
        if(messageProcessingThreadsPool.isEmpty()) {
            throw new IllegalArgumentException("Cannot instantiate broker with an empty pool");
        }
        this.messageProcessingThreadsPool = messageProcessingThreadsPool;
    }

    public synchronized void dispatch(ReceivedMessage receivedMessage) {
        var receivedMessageId = nextReceivedMessageId++;
        ReceivedMessageProcessingState processingState = new ReceivedMessageProcessingState.Builder()
                .numberOfThreads(messageProcessingThreadsPool.size())
                .receivedMessage(receivedMessage)
                .build();
        inProgressProcessingStates.put(receivedMessageId, processingState);
        MessageToProcess messageToProcess = new MessageToProcess.Builder()
                .receivedMessageId(receivedMessageId)
                .receivedMessagePayload(receivedMessage.message())
                .callback(this::signalMessageProcessed)
                .build();
        messageProcessingThreadsPool.submit(messageToProcess);
    }

    private long nextReceivedMessageId = 0;

    private MessageProcessingThreadPool messageProcessingThreadsPool;

    private synchronized void signalMessageProcessed(int threadId, MessageToProcess messageToProcess) {
        var processingProgress = inProgressProcessingStates.get(messageToProcess.receivedMessageId());
        if(processingProgress == null) {
            throw new IllegalArgumentException("No processing state available");
        } else {
            processingProgress.ackThreadProcessed(threadId);
            if(processingProgress.isCompleted()) {
                processingProgress.receivedMessage().ack();
                inProgressProcessingStates.remove(messageToProcess.receivedMessageId());
            }
        }
    }

    private Map<Long, ReceivedMessageProcessingState> inProgressProcessingStates = new HashMap<>();
}
