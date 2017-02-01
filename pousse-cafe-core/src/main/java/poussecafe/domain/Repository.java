package poussecafe.domain;

import poussecafe.storable.ActiveStorableRepository;

public abstract class Repository<A extends AggregateRoot<K, D>, K, D extends AggregateData<K>>
extends ActiveStorableRepository<A, K, D> {

    @Override
    protected void addData(A aggregate) {
        super.addData(aggregate);
        emitAggregateAdded(aggregate);
    }

    protected void emitAggregateAdded(A aggregate) {
        // Should be overridden if an event must be emitted
    }

    @Override
    protected void updateData(A aggregate) {
        super.updateData(aggregate);
        emitAggregateUpdated(aggregate);
    }

    protected void emitAggregateUpdated(A aggregate) {
        // Should be overridden if an event must be emitted
    }

    @Override
    protected void deleteData(A aggregate) {
        super.deleteData(aggregate);
        emitAggregateDeleted(aggregate);
    }

    protected void emitAggregateDeleted(A aggregate) {
        // Should be overridden if an event must be emitted
    }

    @Override
    protected A newStorable() {
        return newAggregate();
    }

    protected abstract A newAggregate();

}
