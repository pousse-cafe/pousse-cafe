package poussecafe.environment;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class TargetAggregates<I> {

    public static class Builder<J> {

        private TargetAggregates<J> aggregates = new TargetAggregates<>();

        public Builder<J> toUpdate(J id) {
            aggregates.toUpdate.add(id);
            return this;
        }

        public Builder<J> toUpdate(Collection<J> ids) {
            aggregates.toUpdate.addAll(ids);
            return this;
        }

        public Builder<J> toCreate(J id) {
            aggregates.toCreate.add(id);
            return this;
        }

        public Builder<J> toCreate(Collection<J> ids) {
            aggregates.toCreate.addAll(ids);
            return this;
        }

        public TargetAggregates<J> build() {
            return aggregates;
        }
    }

    private TargetAggregates() {

    }

    private Set<I> toUpdate = new HashSet<>();

    public Set<I> toUpdate() {
        return Collections.unmodifiableSet(toUpdate);
    }

    private Set<I> toCreate = new HashSet<>();

    public Set<I> toCreate() {
        return Collections.unmodifiableSet(toCreate);
    }
}
