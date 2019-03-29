package poussecafe.environment;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.Repository;
import poussecafe.messaging.Message;
import poussecafe.runtime.TransactionRunnerLocator;
import poussecafe.storage.TransactionRunner;
import poussecafe.util.MethodInvoker;

@SuppressWarnings({"unchecked", "rawtypes"})
public class SingleAggregateCreationMessageConsumer implements Consumer<Message> {

    public static class Builder {

        private SingleAggregateCreationMessageConsumer consumer = new SingleAggregateCreationMessageConsumer();

        public Builder invoker(MethodInvoker invoker) {
            consumer.invoker = invoker;
            return this;
        }

        public Builder transactionRunnerLocator(TransactionRunnerLocator transactionRunnerLocator) {
            consumer.transactionRunnerLocator = transactionRunnerLocator;
            return this;
        }

        public Builder aggregateServices(AggregateServices aggregateServices) {
            consumer.aggregateServices = aggregateServices;
            return this;
        }

        public SingleAggregateCreationMessageConsumer build() {
            Objects.requireNonNull(consumer.transactionRunnerLocator);
            Objects.requireNonNull(consumer.aggregateServices);
            Objects.requireNonNull(consumer.invoker);
            return consumer;
        }
    }

    private SingleAggregateCreationMessageConsumer() {

    }

    @Override
    public void accept(Message message) {
        Object result = invoker.invoke(message);
        AggregateRoot aggregate;
        if(result instanceof Optional) {
            Optional<? extends AggregateRoot> optional = (Optional<? extends AggregateRoot>) result;
            aggregate = optional.orElse(null);
        } else {
            aggregate = (AggregateRoot) result;
        }
        if(aggregate != null) {
            Class aggregateRootEntityClass = aggregateServices.aggregateRootEntityClass();
            Repository repository = aggregateServices.repository();
            TransactionRunner transactionRunner = transactionRunnerLocator.locateTransactionRunner(aggregateRootEntityClass);
            addCreatedAggregate(transactionRunner, repository, aggregate);
        }
    }

    private MethodInvoker invoker;

    private TransactionRunnerLocator transactionRunnerLocator;

    private AggregateServices aggregateServices;

    private void addCreatedAggregate(TransactionRunner transactionRunner,
            Repository repository,
            AggregateRoot aggregate) {
        transactionRunner.runInTransaction(() -> repository.add(aggregate));
    }
}
