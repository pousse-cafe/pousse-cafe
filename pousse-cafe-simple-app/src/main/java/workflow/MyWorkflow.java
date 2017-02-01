package workflow;

import domain.MyAggregate;
import domain.MyDomainEvent;
import domain.MyFactory;
import domain.MyRepository;
import org.slf4j.LoggerFactory;
import poussecafe.consequence.CommandListener;
import poussecafe.consequence.DomainEventListener;
import poussecafe.service.Workflow;

/*
 * This work flow describes how Commands are related to Domain actions. A work flow also describes how Domain Events
 * "link" Aggregates together.
 */
public class MyWorkflow extends Workflow {

    private MyFactory factory;

    private MyRepository repository;

    /*
     * Below listener creates a new Aggregate using related Factory and adds it to the Repository. The creation of the
     * Aggregate does not have to be executed in the context of a transaction. However, the interaction with the
     * Repository must. Any Domain Event added during Aggregate creation are emitted by the Repository upon successful
     * addition of the Aggregate.
     */
    @CommandListener
    public void handle(CreateAggregate command) {
        MyAggregate aggregate = factory.buildAggregate(command.getKey());
        runInTransaction(() -> repository.add(aggregate));
    }

    /*
     * Below listener retrieves targeted Aggregate using the Repository, executes related action and then updates the
     * Aggregate. Some storage technologies like JPA do not require an explicit call to update. However, this is not
     * true in general. As the work flow logic should stay independent of the storage technology, calling "update" is
     * strongly recommended.
     */
    @CommandListener
    public void handle(MyCommand command) {
        runInTransaction(() -> {
            MyAggregate aggregate = repository.get(command.getKey());
            aggregate.doSomeAction(command.getX());
            repository.update(aggregate); // Without this call, update might not be executed with some storage types
        });
    }

    @DomainEventListener
    public void handle(MyDomainEvent event) {
        LoggerFactory.getLogger(getClass()).info("Some action was succesfully executed");
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Below setters are used to inject the factory and repository. Any Domain component (Factory, Repository or Domain
    // Service) is eligible for injection.

    public void setFactory(MyFactory factory) {
        this.factory = factory;
    }

    public void setRepository(MyRepository repository) {
        this.repository = repository;
    }
}
