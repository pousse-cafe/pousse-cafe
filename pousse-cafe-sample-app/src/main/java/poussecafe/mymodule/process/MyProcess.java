package poussecafe.mymodule.process;

import org.slf4j.LoggerFactory;
import poussecafe.discovery.MessageListener;
import poussecafe.mymodule.domain.MyDomainEvent;
import poussecafe.mymodule.domain.myaggregate.MyAggregate;
import poussecafe.mymodule.domain.myaggregate.MyAggregateFactory;
import poussecafe.mymodule.domain.myaggregate.MyAggregateId;
import poussecafe.mymodule.domain.myaggregate.MyAggregateRepository;
import poussecafe.process.DomainProcess;

/*
 * Domain processes are responsible for the execution of operations on aggregates in the context of a DB transaction.
 *
 * Note that explicit DomainProcess definitions are not always required: @MessageListener annotations, defining
 * message listeners, may be put at the level of the aggregate (AggregateRoot sub-class), the factory (Factory sub-class)
 * and the repository (Repository sub-class). In that case, transactions and calls are automatically handled by the
 * runtime.
 */
public class MyProcess extends DomainProcess {

    /*
     * Creates a new aggregate given its id.
     */
    public void createMyAggregate(MyAggregateId id) {
        MyAggregate aggregate = factory.createAggregate(id);
        runInTransaction(MyAggregate.class, () -> repository.add(aggregate));
    }

    private MyAggregateFactory factory;

    /*
     * Updates a given aggregate in the context of a transaction. It is not a message listener so
     * it must be called explicitly by the application.
     */
    public void doSomeAction(DoSomethingParameters command) {
        runInTransaction(MyAggregate.class, () -> {
            MyAggregate aggregate = repository.get(command.id);
            aggregate.doSomething(command.x);
            repository.update(aggregate);
        });
    }

    public static class DoSomethingParameters {

        public MyAggregateId id;

        public int x;
    }

    private MyAggregateRepository repository;

    /*
     * Message listener logging an event.
     */
    @MessageListener
    public void handle(MyDomainEvent event) {
        LoggerFactory.getLogger(getClass()).info("Some action was succesfully executed on {}", event.identifier().value());
    }
}
