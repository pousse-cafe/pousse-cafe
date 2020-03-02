package poussecafe.processing;

import java.util.Objects;
import poussecafe.runtime.OriginalAndMarshaledMessage;

class MessageToProcess {

    public static class Builder {

        private MessageToProcess message = new MessageToProcess();

        public Builder receivedMessageId(long receivedMessageId) {
            message.receivedMessageId = receivedMessageId;
            return this;
        }

        public Builder receivedMessagePayload(OriginalAndMarshaledMessage receivedMessagePayload) {
            message.receivedMessagePayload = receivedMessagePayload;
            return this;
        }

        public Builder callback(Callback callback) {
            message.callback = callback;
            return this;
        }

        public MessageToProcess build() {
            Objects.requireNonNull(message.receivedMessageId);
            Objects.requireNonNull(message.receivedMessagePayload);
            Objects.requireNonNull(message.callback);
            return message;
        }
    }

    private MessageToProcess() {

    }

    private long receivedMessageId;

    public long receivedMessageId() {
        return receivedMessageId;
    }

    private OriginalAndMarshaledMessage receivedMessagePayload;

    public OriginalAndMarshaledMessage receivedMessagePayload() {
        return receivedMessagePayload;
    }

    public void signalProcessed(int threadId) {
        callback.signalProcessed(threadId, this);
    }

    public static interface Callback {

        void signalProcessed(int threadId, MessageToProcess processedMessage);
    }

    private Callback callback;
}
