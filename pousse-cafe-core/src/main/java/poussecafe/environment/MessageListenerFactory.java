package poussecafe.environment;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.DomainException;
import poussecafe.domain.Factory;
import poussecafe.domain.Repository;
import poussecafe.exception.PousseCafeException;
import poussecafe.messaging.AggregateMessageListenerRunner;
import poussecafe.messaging.Message;
import poussecafe.messaging.MessageListener;
import poussecafe.messaging.MessageListenerDefinition;
import poussecafe.process.DomainProcess;
import poussecafe.runtime.TransactionRunnerLocator;
import poussecafe.storage.TransactionRunner;
import poussecafe.util.ReflectionUtils;

@SuppressWarnings({"rawtypes", "unchecked"})
public class MessageListenerFactory {

    public static class Builder {

        private MessageListenerFactory factory = new MessageListenerFactory();

        public Builder environment(Environment environment) {
            factory.environment = environment;
            return this;
        }

        public Builder transactionRunnerLocator(TransactionRunnerLocator transactionRunnerLocator) {
            factory.transactionRunnerLocator = transactionRunnerLocator;
            return this;
        }

        public MessageListenerFactory build() {
            Objects.requireNonNull(factory.environment);
            Objects.requireNonNull(factory.transactionRunnerLocator);
            return factory;
        }
    }

    public MessageListener build(MessageListenerDefinition definition) {
        if(DomainProcess.class.isAssignableFrom(definition.method().getDeclaringClass())) {
            return buildDomainProcessListener(definition);
        } else if(Factory.class.isAssignableFrom(definition.method().getDeclaringClass())) {
            return buildFactoryListener(definition);
        } else if(Repository.class.isAssignableFrom(definition.method().getDeclaringClass())) {
            return buildRepositoryListener(definition);
        } else if(AggregateRoot.class.isAssignableFrom(definition.method().getDeclaringClass())) {
            return buildAggregateRootListener(definition);
        } else {
            throw new PousseCafeException("Unsupported message listener definition");
        }
    }

    private MessageListener buildDomainProcessListener(MessageListenerDefinition definition) {
        Method method = definition.method();
        DomainProcess target = environment.domainProcess((Class<? extends DomainProcess>) definition.method().getDeclaringClass()).orElseThrow(PousseCafeException::new);
        return buildMessageListenerBuilder(definition)
                .consumer(buildMessageConsumer(target, method))
                .build();
    }

    private MessageListener.Builder buildMessageListenerBuilder(MessageListenerDefinition definition) {
        Method method = definition.method();
        Class<? extends Message> messageClass = (Class<? extends Message>) method.getParameters()[0].getType();
        Optional<String> customId = definition.customId();
        if(!customId.isPresent()) {
            customId = Optional.of(new DeclaredMessageListenerIdBuilder()
                    .messageClass(messageClass)
                    .method(method)
                    .build());
        }
        return new MessageListener.Builder()
                .id(customId.get())
                .messageClass(messageClass);
    }

    private Environment environment;

    private Consumer<Message> buildMessageConsumer(Object target,
            Method method) {
        return message -> {
            try {
                method.invoke(target, message);
            } catch (Exception e) {
                throw new PousseCafeException("Unable to invoke declared listener", e);
            }
        };
    }

    private MessageListener buildFactoryListener(MessageListenerDefinition definition) {
        Factory target = environment.factory(definition.method().getDeclaringClass()).orElseThrow(PousseCafeException::new);
        return buildMessageListenerBuilder(definition)
                .consumer(buildFactoryListenerConsumer(target, definition))
                .build();
    }

    private Consumer<Message> buildFactoryListenerConsumer(Factory factory, MessageListenerDefinition definition) {
        Class entityClass = factory.entityClass();
        Repository repository = environment.repositoryOf(entityClass).orElseThrow(PousseCafeException::new);
        Method method = definition.method();
        Class<?> returnType = method.getReturnType();
        if(entityClass.isAssignableFrom(returnType) || returnType.isAssignableFrom(Optional.class)) {
            return addSingleCreatedAggregate(factory, method, repository, entityClass);
        } else if(Iterable.class.isAssignableFrom(returnType)) {
            return addIterableCreatedAggregates(factory, method, repository, entityClass);
        } else {
            throw new PousseCafeException("Method " + method.getName() + " of " + factory.getClass().getName() + " is not a valid factory message listener");
        }
    }

    private Consumer<Message> addSingleCreatedAggregate(Object target,
            Method method,
            Repository repository,
            Class entityClass) {
        return message -> {
            try {
                Object result = method.invoke(target, message);
                AggregateRoot aggregate;
                if(result instanceof Optional) {
                    Optional<? extends AggregateRoot> optional = (Optional<? extends AggregateRoot>) result;
                    aggregate = optional.orElse(null);
                } else {
                    aggregate = (AggregateRoot) result;
                }
                if(aggregate != null) {
                    TransactionRunner transactionRunner = transactionRunnerLocator.locateTransactionRunner(entityClass);
                    addCreatedAggregate(transactionRunner, repository, aggregate);
                }
            } catch (Exception e) {
                throw new PousseCafeException("Unable to build new aggregate", e);
            }
        };
    }

    private TransactionRunnerLocator transactionRunnerLocator;

    private void addCreatedAggregate(TransactionRunner transactionRunner,
            Repository repository,
            AggregateRoot aggregate) {
        transactionRunner.runInTransaction(() -> repository.add(aggregate));
    }

    private Consumer<Message> addIterableCreatedAggregates(Object target,
            Method method,
            Repository repository,
            Class entityClass) {
        return message -> {
            try {
                Iterable<AggregateRoot> iterable = (Iterable<AggregateRoot>) method.invoke(target, message);
                TransactionRunner transactionRunner = transactionRunnerLocator.locateTransactionRunner(entityClass);
                for(AggregateRoot aggregate : iterable) {
                    addCreatedAggregate(transactionRunner, repository, aggregate);
                }
            } catch (Exception e) {
                throw new PousseCafeException("Unable to build new aggregate", e);
            }
        };
    }

    private MessageListener buildRepositoryListener(MessageListenerDefinition definition) {
        Method method = definition.method();
        Repository target = environment.repository(definition.method().getDeclaringClass()).orElseThrow(PousseCafeException::new);
        return buildMessageListenerBuilder(definition)
                .consumer(buildRepositoryMessageConsumer(target, method))
                .build();
    }

    private Consumer<Message> buildRepositoryMessageConsumer(Object target,
            Method method) {
        Repository factory = (Repository) target;
        Class<?> entityClass = factory.entityClass();
        return message -> {
            TransactionRunner transactionRunner = transactionRunnerLocator.locateTransactionRunner(entityClass);
            transactionRunner.runInTransaction(() -> {
                try {
                    method.invoke(target, message);
                } catch (Exception e) {
                    throw new PousseCafeException("Unable to invoke repository method", e);
                }
            });
        };
    }

    private MessageListener buildAggregateRootListener(MessageListenerDefinition definition) {
        Class<? extends AggregateMessageListenerRunner> runnerClass = definition.runner().orElseThrow(() -> new DomainException("Aggregate root message listeners must have a runner"));
        AggregateMessageListenerRunner runner = ReflectionUtils.newInstance(runnerClass);
        return buildMessageListenerBuilder(definition)
                .consumer(buildAggregateRootMessageConsumer(definition, runner))
                .runner(Optional.of(runner))
                .build();
    }

    protected Consumer<Message> buildAggregateRootMessageConsumer(MessageListenerDefinition definition, AggregateMessageListenerRunner runner) {
        Method method = definition.method();
        Class entityClass = method.getDeclaringClass();
        AggregateServices entityServices = environment.aggregateServicesOf(entityClass).orElseThrow(PousseCafeException::new);
        return message -> {
            Set targetAggregatesKey = runner.targetAggregatesKeys(message);
            Repository repository = entityServices.getRepository();
            for(Object key : targetAggregatesKey) {
                TransactionRunner transactionRunner = transactionRunnerLocator.locateTransactionRunner(entityClass);
                transactionRunner.runInTransaction(() -> {
                    AggregateRoot targetAggregateRoot = repository.get(key);
                    targetAggregateRoot.context(runner.context(message, targetAggregateRoot));
                    try {
                        method.invoke(targetAggregateRoot, message);
                    } catch (Exception e) {
                        throw new PousseCafeException("Unable to invoke listener", e);
                    }
                    repository.update(targetAggregateRoot);
                });
            }
        };
    }
}
