package poussecafe.storage.internal.uniqueindex;

import java.util.Optional;

import static poussecafe.check.Checks.checkThatValue;

public class UpdatePlan implements Plan {

    public static class Builder {

        private UpdatePlan plan = new UpdatePlan();

        public Builder newUniqueData(Object newUniqueData) {
            plan.newUniqueData = newUniqueData;
            return this;
        }

        public Builder oldUniqueData(Optional<Object> oldUniqueData) {
            plan.oldUniqueData = oldUniqueData;
            return this;
        }

        public Builder index(UniqueIndex<?> index) {
            plan.index = index;
            return this;
        }

        public UpdatePlan build() {
            checkThatValue(plan.newUniqueData).notNull();
            checkThatValue(plan.oldUniqueData).notNull();
            checkThatValue(plan.index).notNull();
            return plan;
        }
    }

    private UpdatePlan() {

    }

    private Object newUniqueData;

    public Object newUniqueData() {
        return newUniqueData;
    }

    private Optional<Object> oldUniqueData;

    public Optional<Object> oldUniqueData() {
        return oldUniqueData;
    }

    private UniqueIndex<?> index;

    @Override
    public void commit() {
        index.commit(this);
    }
}
