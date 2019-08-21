package poussecafe.mymodule.domain.myaggregate;

import poussecafe.discovery.MessageListener;
import poussecafe.domain.Factory;
import poussecafe.mymodule.domain.ADomainEvent;

/*
 * The Factory is responsible for providing an Aggregate instance.
 * It also exposes any means of building an aggregate in a consistent initial state.
 */
public class MyAggregateFactory extends Factory<MyAggregateId, MyAggregate, MyAggregate.Attributes> {

    /*
     * One way to create a MyAggregate instance is to directly call below method.
     */
    public MyAggregate createAggregate(MyAggregateId id) {
        return newAggregateWithId(id);
    }

    /*
     * A ADomainEvent event also triggers the creation. This does not require the definition of an explicit
     * DomainProcess.
     */
    @MessageListener
    public MyAggregate createAggregate(ADomainEvent event) {
        return createAggregate(event.identifier().value());
    }
}
