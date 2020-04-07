package poussecafe.processing;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@SuppressWarnings("rawtypes")
public class DefaultProcessingThreadSelector implements ProcessingThreadSelector {

    public DefaultProcessingThreadSelector(int poolSize) {
        threadLoad = new int[poolSize];
    }

    @Override
    public int selectFor(MessageListenersGroup group) {
        Optional<Class> aggregateRootClass = group.aggregateRootClass();
        int threadId;
        if(aggregateRootClass.isPresent()) {
            threadId = queueGroupInAssignment(aggregateRootClass.get());
        } else {
            threadId = leastLoaded();
        }

        threadLoad[threadId] = threadLoad[threadId] + group.listeners().size();
        return threadId;
    }

    private int queueGroupInAssignment(Class aggregateRootClass) {
        int threadId;
        ThreadAssignment assignment = aggregateToAssignment.get(aggregateRootClass);
        if(assignment == null) {
            threadId = leastLoaded();
            assignment = new ThreadAssignment(threadId);
            aggregateToAssignment.put(aggregateRootClass, assignment);
        } else {
            threadId = assignment.threadId();
        }

        assignment.queueGroup();
        return threadId;
    }

    private Map<Class, ThreadAssignment> aggregateToAssignment = new HashMap<>();

    private int[] threadLoad;

    private int leastLoaded() {
        int lowestLoad = threadLoad[0];
        int leastLoadedThreadId = 0;
        for(int i = 1; i < threadLoad.length; ++i) {
            int load = threadLoad[i];
            if(load < lowestLoad) {
                lowestLoad = load;
                leastLoadedThreadId = i;
            }
        }
        return leastLoadedThreadId;
    }

    @Override
    public void unselect(int threadId, MessageListenersGroup messageListenerGroup) {
        Optional<Class> aggregateRootClass = messageListenerGroup.aggregateRootClass();
        if(aggregateRootClass.isPresent()) {
            unqueueGroupInAssignment(threadId, aggregateRootClass.get());
        }

        threadLoad[threadId] = threadLoad[threadId] - messageListenerGroup.listeners().size();
    }

    private void unqueueGroupInAssignment(int threadId, Class aggregateRootClass) {
        ThreadAssignment assignment = aggregateToAssignment.get(aggregateRootClass);
        if(assignment == null) {
            throw new IllegalArgumentException("No assignment found for aggregate " + aggregateRootClass
                    + " and threadId " + threadId);
        }
        if(assignment.threadId() != threadId) {
            throw new IllegalArgumentException("Unexpected assignment");
        }
        assignment.unqueueGroup();
        if(assignment.noGroupsQueued()) {
            aggregateToAssignment.remove(aggregateRootClass);
        }
    }

    public Optional<Integer> queuedGroupsFor(Class aggregateRootClass) {
        return Optional.ofNullable(aggregateToAssignment.get(aggregateRootClass)).map(ThreadAssignment::queuedGroups);
    }

    public int loadOf(int threadId) {
        return threadLoad[threadId];
    }
}
