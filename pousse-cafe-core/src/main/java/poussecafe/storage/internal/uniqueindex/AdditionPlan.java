package poussecafe.storage.internal.uniqueindex;

import java.util.Objects;

public class AdditionPlan implements Plan {

    public static class Builder {

        private AdditionPlan plan = new AdditionPlan();

        public Builder index(UniqueIndex<?> index) {
            plan.index = index;
            return this;
        }

        public Builder uniqueData(Object uniqueData) {
            plan.uniqueData = uniqueData;
            return this;
        }

        public AdditionPlan build() {
            Objects.requireNonNull(plan.uniqueData);
            Objects.requireNonNull(plan.index);
            return plan;
        }

    }

    private AdditionPlan() {

    }

    private Object uniqueData;

    public Object uniqueData() {
        return uniqueData;
    }

    private UniqueIndex<?> index;

    @Override
    public void commit() {
        index.commit(this);
    }
}
