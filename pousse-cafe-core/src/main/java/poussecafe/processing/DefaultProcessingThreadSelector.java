package poussecafe.processing;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class DefaultProcessingThreadSelector implements ProcessingThreadSelector {

    public DefaultProcessingThreadSelector(int poolSize) {
        threadLoad = new int[poolSize];
    }

    @Override
    @SuppressWarnings("rawtypes")
    public int selectFor(MessageListenersGroup group) {
        Optional<Class> aggregateRootClass = group.aggregateRootClass();
        if(aggregateRootClass.isPresent()) {
            Integer threadId = aggregateToThreadId.get(aggregateRootClass.get());
            if(threadId == null) {
                threadId = leastLoaded();
                ++threadLoad[threadId];
            }
            return threadId;
        } else {
            return leastLoaded();
        }
    }

    @SuppressWarnings("rawtypes")
    private Map<Class, Integer> aggregateToThreadId = new HashMap<>();

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
}
