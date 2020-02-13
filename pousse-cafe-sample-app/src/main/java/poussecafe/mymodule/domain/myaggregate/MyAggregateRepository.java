package poussecafe.mymodule.domain.myaggregate;

import poussecafe.discovery.MessageListener;
import poussecafe.domain.Repository;
import poussecafe.mymodule.domain.YetAnotherDomainEvent;

/*
 * The Repository is responsible for reading, updating or removing aggregates.
 */
public class MyAggregateRepository extends Repository<MyAggregate, MyAggregateId, MyAggregate.Attributes> {

    /*
     * This message listener deletes an aggregate when a YetAnotherDomainEvent event is consumed.
     */
    @MessageListener
    public void remove(YetAnotherDomainEvent event) {
        delete(event.identifier().value());
    }
}
