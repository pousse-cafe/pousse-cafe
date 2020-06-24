package poussecafe.domain;

import poussecafe.runtime.ActiveAggregate;

public abstract class AggregateRoot<K, D extends EntityAttributes<K>> extends Entity<K, D> {

    public void onAdd() {

    }

    public void onUpdate() {

    }

    public void onDelete() {

    }

    @Override
    public D attributes() {
        ActiveAggregate.instance().set(this);
        return super.attributes();
    }
}
