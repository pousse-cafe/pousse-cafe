package poussecafe.domain;

public abstract class AggregateRoot<K, D extends EntityAttributes<K>> extends Entity<K, D> {

    public void onAdd() {

    }

    public void onUpdate() {

    }

    public void onDelete() {

    }
}
