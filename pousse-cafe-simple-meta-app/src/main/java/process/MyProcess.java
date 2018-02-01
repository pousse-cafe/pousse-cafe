package process;

import domain.MyAggregate;
import domain.MyDomainEvent;
import domain.MyFactory;
import domain.MyRepository;
import org.slf4j.LoggerFactory;
import poussecafe.messaging.DomainEventListener;
import poussecafe.service.DomainProcess;

/*
 * This work flow describes how Commands are related to Domain actions. A work flow also describes how Domain Events
 * "link" Aggregates together.
 */
public class MyProcess extends DomainProcess {

    private MyFactory factory;

    private MyRepository repository;

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
