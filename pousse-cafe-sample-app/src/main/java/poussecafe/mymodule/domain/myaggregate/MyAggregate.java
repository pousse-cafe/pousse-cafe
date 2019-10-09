package poussecafe.mymodule.domain.myaggregate;

import poussecafe.attribute.Attribute;
import poussecafe.discovery.Aggregate;
import poussecafe.discovery.MessageListener;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.DomainException;
import poussecafe.domain.EntityAttributes;
import poussecafe.mymodule.domain.AnotherDomainEvent;
import poussecafe.mymodule.domain.MyDomainEvent;

/*
 * A simple aggregate root
 */
@Aggregate(
    factory = MyAggregateFactory.class,
    repository = MyAggregateRepository.class
)
public class MyAggregate extends AggregateRoot<MyAggregateId, MyAggregate.Attributes> {

    /*
     * Below action updates aggregate's state and triggers the emission of a Domain Event in case of success
     */
    public void doSomething(int x) {
        if(x <= 0) {
            throw new DomainException("X cannot be <=0");
        }

        // Update attribute's value
        attributes().x().value(x);

        // Emit a domain event telling the attribute has been updated in the context of doSomething.
        MyDomainEvent event = newDomainEvent(MyDomainEvent.class);
        event.identifier().valueOf(attributes().identifier());
        emitDomainEvent(event);
    }

    /*
     * Below method is a listener to AnotherDomainEvent event. This does not require the definition of an explicit
     * DomainProcess. However, a runner service has to be provided. It is responsible for
     * - the extraction of aggregate ids out of the event.
     * - providing a context to the execution of the listener (none in this case).
     */
    @MessageListener(runner = DoSomethingElseRunner.class)
    public void doSomething(AnotherDomainEvent event) {
        doSomething(event.x().value());
    }

    /*
     * This interface defines the data model in the form of a set of attributes without exposing implementation details
     * (JPA Entity, Mongo document, etc).
     */
    public interface Attributes extends EntityAttributes<MyAggregateId> {

        /*
         * An Attribute instance allows to read or update the value.
         */
        Attribute<Integer> x();
    }

}
