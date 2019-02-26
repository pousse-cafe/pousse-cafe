package poussecafe.simple.process;

import org.slf4j.LoggerFactory;
import poussecafe.messaging.DomainEventListener;
import poussecafe.process.DomainProcess;
import poussecafe.simple.domain.MyAggregate;
import poussecafe.simple.domain.MyAggregateFactory;
import poussecafe.simple.domain.MyAggregateRepository;
import poussecafe.simple.domain.MyDomainEvent;

/*
 * This work flow describes how Commands are related to Domain actions. A work flow also describes how Domain Events
 * "link" Aggregates together.
 */
public class MyProcess extends DomainProcess {

    private MyAggregateFactory factory;

    private MyAggregateRepository repository;

    public void handle(CreateAggregate command) {
        MyAggregate aggregate = factory.buildAggregate(command.getKey());
        runInTransaction(MyAggregate.class, () -> repository.add(aggregate));
    }

    public void handle(MyCommand command) {
        runInTransaction(MyAggregate.class, () -> {
            MyAggregate aggregate = repository.get(command.getKey());
            aggregate.doSomeAction(command.getX());
            repository.update(aggregate); // Without this call, update might not be executed with some storage types
        });
    }

    @DomainEventListener
    public void handle(MyDomainEvent event) {
        LoggerFactory.getLogger(getClass()).info("Some action was succesfully executed");
    }
}
