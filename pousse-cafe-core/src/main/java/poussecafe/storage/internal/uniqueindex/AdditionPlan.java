package poussecafe.storage.internal.uniqueindex;

import static poussecafe.check.Checks.checkThatValue;

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
            checkThatValue(plan.uniqueData).notNull();
            checkThatValue(plan.index).notNull();
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
