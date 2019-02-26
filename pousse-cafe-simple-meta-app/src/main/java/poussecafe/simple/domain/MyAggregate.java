package poussecafe.simple.domain;

import poussecafe.attribute.Attribute;
import poussecafe.discovery.Aggregate;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.EntityAttributes;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;
import static poussecafe.check.Predicates.greaterThan;

/*
 * A simple aggregate root
 */
@Aggregate(
    factory = MyAggregateFactory.class,
    repository = MyAggregateRepository.class
)
public class MyAggregate extends AggregateRoot<MyAggregateKey, MyAggregate.Attributes> {

    /*
     * Below action updates aggregate's state and triggers the emission of a Domain Event in case of success
     */
    public void doSomeAction(int x) {
        checkThat(value(x).verifies(greaterThan(0)).because("X cannot be <=0"));
        attributes().x().value(x);

        MyDomainEvent event = newDomainEvent(MyDomainEvent.class);
        event.key().valueOf(attributes().key());
        addDomainEvent(event);
    }

    /*
     * This interface defines the data model in the form of a set of properties without exposing implementation details
     * (JPA Entity, Mongo document, POJO, etc).
     */
    public interface Attributes extends EntityAttributes<MyAggregateKey> {

        /*
         * A Attribute instance allows to get or set the value of the property.
         */
        Attribute<Integer> x();
    }

}
