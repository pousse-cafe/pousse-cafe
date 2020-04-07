package poussecafe.processing;

public class ThreadAssignment {

    public ThreadAssignment(int threadId) {
        this.threadId = threadId;
    }

    private int threadId;

    public int threadId() {
        return threadId;
    }

    private int queuedGroups;

    public void queueGroup() {
        ++queuedGroups;
    }

    public void unqueueGroup() {
        --queuedGroups;
    }

    public boolean noGroupsQueued() {
        return queuedGroups == 0;
    }

    public int queuedGroups() {
        return queuedGroups;
    }
}
