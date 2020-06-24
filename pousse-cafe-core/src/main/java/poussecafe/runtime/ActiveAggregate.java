package poussecafe.runtime;

import poussecafe.domain.AggregateRoot;

@SuppressWarnings("rawtypes")
public class ActiveAggregate {

    public void set(AggregateRoot root) {
        aggregate.set(root);
    }

    private ThreadLocal<AggregateRoot> aggregate = new ThreadLocal<>();

    public AggregateRoot get() {
        AggregateRoot root = aggregate.get();
        if(root == null) {
            throw new IllegalStateException("No aggregate root registered");
        }
        return root;
    }

    public void clear() {
        aggregate.remove();
    }

    public static ActiveAggregate instance() {
        return SINGLETON;
    }

    private static final ActiveAggregate SINGLETON = new ActiveAggregate();

    private ActiveAggregate() {

    }
}
