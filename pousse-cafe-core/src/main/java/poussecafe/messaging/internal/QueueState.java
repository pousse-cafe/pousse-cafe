package poussecafe.messaging.internal;

class QueueState {

    private int queuedMessages;

    private int messagesBeingProcessed;

    private boolean interrupted;

    public synchronized void addQueuedMessage() {
        if(!interrupted) {
            ++queuedMessages;
        }
    }

    public synchronized void removeQueuedMessage() {
        if(!interrupted) {
            --queuedMessages;
        }
    }

    public synchronized void addMessageBeingProcessing() {
        if(!interrupted) {
            ++messagesBeingProcessed;
        }
    }

    public synchronized void removeMessageBeingProcessing() {
        if(!interrupted) {
            --messagesBeingProcessed;
        }
    }

    public synchronized boolean emptyOrInterrupted() {
        return interrupted || (queuedMessages == 0 && messagesBeingProcessed == 0);
    }

    public synchronized void interrupt() {
        interrupted = true;
    }

    public synchronized boolean interrupted() {
        return interrupted;
    }
}
