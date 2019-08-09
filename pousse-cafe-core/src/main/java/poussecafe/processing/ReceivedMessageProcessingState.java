package poussecafe.processing;

import java.util.Objects;

class ReceivedMessageProcessingState {

    public static class Builder {

        private ReceivedMessageProcessingState state = new ReceivedMessageProcessingState();

        public Builder numberOfThreads(int numberOfThreads) {
            this.numberOfThreads = numberOfThreads;
            return this;
        }

        private int numberOfThreads = -1;

        public Builder receivedMessage(ReceivedMessage receivedMessage) {
            state.receivedMessage = receivedMessage;
            return this;
        }

        public ReceivedMessageProcessingState build() {
            Objects.requireNonNull(state.receivedMessage);
            state.processed = new boolean[numberOfThreads];
            return state;
        }
    }

    private ReceivedMessageProcessingState() {

    }

    private boolean[] processed;

    public void ackThreadProcessed(int threadIndex) {
        if(processed[threadIndex]) {
            throw new IllegalArgumentException("Processing already acked for thread " + threadIndex);
        }
        processed[threadIndex] = true;
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
