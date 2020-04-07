package poussecafe.processing;

import java.util.Objects;

class MessageToProcess {

    public static class Builder {

        private MessageToProcess message = new MessageToProcess();

        public Builder receivedMessageId(long receivedMessageId) {
            message.receivedMessageId = receivedMessageId;
            return this;
        }

        public Builder messageListenerGroup(MessageListenersGroup messageListenerGroup) {
            message.messageListenerGroup = messageListenerGroup;
            return this;
        }

        public Builder callback(Callback callback) {
            message.callback = callback;
            return this;
        }

        public MessageToProcess build() {
            Objects.requireNonNull(message.receivedMessageId);
            Objects.requireNonNull(message.messageListenerGroup);
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

    private MessageListenersGroup messageListenerGroup;

    public MessageListenersGroup messageListenerGroup() {
        return messageListenerGroup;
    }

    public void signalProcessed(int threadId) {
        callback.signalProcessed(threadId, this);
    }

    public static interface Callback {

        void signalProcessed(int threadId, MessageToProcess processedMessage);
    }

    private Callback callback;
}
