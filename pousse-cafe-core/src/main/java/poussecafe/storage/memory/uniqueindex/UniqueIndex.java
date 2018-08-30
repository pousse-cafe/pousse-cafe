package poussecafe.storage.memory.uniqueindex;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import static poussecafe.check.Checks.checkThatValue;

public class UniqueIndex<D> {

    public static class Builder<E> {

        private UniqueIndex<E> index = new UniqueIndex<>();

        public Builder<E> uniqueDataProducer(Function<E, Object> uniqueDataProducer) {
            index.uniqueDataProducer = uniqueDataProducer;
            return this;
        }

        public Builder<E> name(String name) {
            index.name = name;
            return this;
        }

        public UniqueIndex<E> build() {
            checkThatValue(index.name).notNull();
            checkThatValue(index.uniqueDataProducer).notNull();
            return index;
        }
    }

    private UniqueIndex() {

    }

    private String name;

    public String name() {
        return name;
    }

    private Set<Object> index = new HashSet<>();

    private Function<D, Object> uniqueDataProducer;

    public AdditionPlan prepareAddition(D data) {
        Object uniqueData = uniqueDataProducer.apply(data);
        checkAddition(uniqueData);
        return new AdditionPlan.Builder()
                .index(this)
                .uniqueData(uniqueData)
                .build();
    }

    private void checkAddition(Object uniqueData) {
        if(index.contains(uniqueData)) {
            throw new UniqueIndexException("Provided data violate uniqueness contraint " + name);
        }
    }

    void commit(AdditionPlan plan) {
        checkAddition(plan);
        index.add(plan.uniqueData());
    }

    public UpdatePlan prepareUpdate(Optional<D> oldData, D newData) {
        Object newUniqueData = uniqueDataProducer.apply(newData);
        Optional<Object> oldUniqueData = oldData.map(uniqueDataProducer::apply);
        checkUpdate(newUniqueData, oldUniqueData);
        return new UpdatePlan.Builder()
                .index(this)
                .newUniqueData(newUniqueData)
                .oldUniqueData(oldUniqueData)
                .build();
    }

    private void checkUpdate(Object newUniqueData,
            Optional<Object> oldUniqueData) {
        if(!oldUniqueData.isPresent() || !oldUniqueData.get().equals(newUniqueData)) {
            checkAddition(newUniqueData);
        }
    }

    void commit(UpdatePlan plan) {
        checkUpdate(plan.newUniqueData(), plan.oldUniqueData());
        index.add(plan.newUniqueData());
    }
}
