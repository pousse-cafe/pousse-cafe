package domain;

import poussecafe.domain.AggregateRoot;
import poussecafe.storable.ActiveStorableData;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;
import static poussecafe.check.Predicates.greaterThan;

/*
 * A simple aggregate root
 */
public class MyAggregate extends AggregateRoot<MyAggregateKey, MyAggregate.Data> {

    /*
     * Below action updates aggregate's state and triggers the emission of a Domain Event in case of success
     */
    public void doSomeAction(int x) {
        checkThat(value(x).verifies(greaterThan(0)).because("X cannot be <=0"));
        getData().setX(x);
        addDomainEvent(new MyDomainEvent(getKey()));
    }

    /*
     * Below getter publicly exposes a data field.
     */
    public int getX() {
        return getData().getX();
    }

    /*
     * This interface defines the data model without exposing implementation details (JPA Entity, Mongo document, POJO,
     * etc).
     */
    public interface Data extends ActiveStorableData<MyAggregateKey> {

        void setX(int x);

        int getX();

    }

}
