package poussecafe.storage.internal.uniqueindex;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import java.util.Objects;

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

        public Builder<E> sparse(boolean sparse) {
            index.sparse = sparse;
            return this;
        }

        public UniqueIndex<E> build() {
            Objects.requireNonNull(index.name);
            Objects.requireNonNull(index.uniqueDataProducer);
            return index;
        }
    }

    private UniqueIndex() {

    }

    private String name;

    public String name() {
        return name;
    }

    private boolean sparse;

    public boolean sparse() {
        return sparse;
    }

    private Set<Object> index = new HashSet<>();

    private Function<D, Object> uniqueDataProducer;

    public Optional<AdditionPlan> prepareAddition(D data) {
        Object uniqueData = uniqueDataProducer.apply(data);
        if(uniqueData == null && sparse) {
            return Optional.empty();
        } else {
            checkAddition(uniqueData);
            return Optional.of(new AdditionPlan.Builder()
                    .index(this)
                    .uniqueData(uniqueData)
                    .build());
        }
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

    public Optional<UpdatePlan> prepareUpdate(Optional<D> oldData, D newData) {
        Object newUniqueData = uniqueDataProducer.apply(newData);
        if(newUniqueData == null && sparse) {
            return Optional.empty();
        } else {
            Optional<Object> oldUniqueData = oldData.map(uniqueDataProducer::apply);
            checkUpdate(newUniqueData, oldUniqueData);
            return Optional.of(new UpdatePlan.Builder()
                    .index(this)
                    .newUniqueData(newUniqueData)
                    .oldUniqueData(oldUniqueData)
                    .build());
        }
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
