package poussecafe.processing;

import java.util.Objects;

class ReceivedMessageProcessingState {

    public static class Builder {

        private ReceivedMessageProcessingState state = new ReceivedMessageProcessingState();

        public Builder receivedMessage(ReceivedMessage receivedMessage) {
            state.receivedMessage = receivedMessage;
            return this;
        }

        public Builder firstId(long firstId) {
            state.firstId = firstId;
            return this;
        }

        public Builder sequenceLength(int sequenceLength) {
            this.sequenceLength = sequenceLength;
            return this;
        }

        private int sequenceLength;

        public ReceivedMessageProcessingState build() {
            Objects.requireNonNull(state.receivedMessage);
            state.processed = new boolean[sequenceLength];
            return state;
        }
    }

    private ReceivedMessageProcessingState() {

    }

    private long firstId;

    private boolean[] processed;

    public void ackReceivedMessageId(long receivedMessageId) {
        int index = (int) (receivedMessageId - firstId);
        if(processed[index]) {
            throw new IllegalArgumentException("Processing already acked for ID " + index);
        }
        processed[index] = true;
    }

    public boolean isCompleted() {
        for(int i = 0; i < processed.length; ++i) {
            if(!processed[i]) {
                return false;
            }
        }
        return true;
    }

    public ReceivedMessage receivedMessage() {
        return receivedMessage;
    }

    private ReceivedMessage receivedMessage;
}
