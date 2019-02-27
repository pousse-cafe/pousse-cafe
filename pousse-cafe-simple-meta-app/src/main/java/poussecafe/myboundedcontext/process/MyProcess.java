package poussecafe.myboundedcontext.process;

import org.slf4j.LoggerFactory;
import poussecafe.messaging.DomainEventListener;
import poussecafe.myboundedcontext.domain.MyAggregate;
import poussecafe.myboundedcontext.domain.MyAggregateFactory;
import poussecafe.myboundedcontext.domain.MyAggregateKey;
import poussecafe.myboundedcontext.domain.MyAggregateRepository;
import poussecafe.myboundedcontext.domain.MyDomainEvent;
import poussecafe.process.DomainProcess;

/*
 * This work flow describes how Commands are related to Domain actions. A work flow also describes how Domain Events
 * "link" Aggregates together.
 */
public class MyProcess extends DomainProcess {

    private MyAggregateFactory factory;

    private MyAggregateRepository repository;

    public void createMyAggregate(MyAggregateKey key) {
        MyAggregate aggregate = factory.buildAggregate(key);
        runInTransaction(MyAggregate.class, () -> repository.add(aggregate));
    }

    public void doSomeAction(DoSomeActionParameters command) {
        runInTransaction(MyAggregate.class, () -> {
            MyAggregate aggregate = repository.get(command.key);
            aggregate.doSomeAction(command.x);
            repository.update(aggregate); // Without this call, update might not be executed with some storage types
        });
    }

    @DomainEventListener
    public void handle(MyDomainEvent event) {
        LoggerFactory.getLogger(getClass()).info("Some action was succesfully executed");
    }
}
