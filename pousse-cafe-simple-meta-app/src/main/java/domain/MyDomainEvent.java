package domain;
import poussecafe.domain.DomainEvent;

/*
 * This Domain Event is emitted by a MyAggregate instance.
 */
public class MyDomainEvent implements DomainEvent {

    private MyAggregateKey key;

    public MyDomainEvent(MyAggregateKey key) {
        setKey(key);
    }

    public MyAggregateKey getKey() {
        return key;
    }

    private void setKey(MyAggregateKey key) {
        this.key = key;
    }

}
