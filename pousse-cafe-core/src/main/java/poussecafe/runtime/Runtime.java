package poussecafe.runtime;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import poussecafe.discovery.CustomMessageListenerDiscoverer;
import poussecafe.domain.DomainEvent;
import poussecafe.environment.BoundedContext;
import poussecafe.environment.Environment;
import poussecafe.environment.EnvironmentBuilder;
import poussecafe.injector.Injector;
import poussecafe.messaging.Messaging;
import poussecafe.messaging.MessagingConnection;
import poussecafe.processing.MessageBroker;
import poussecafe.processing.MessageProcessingThreadPool;
import poussecafe.storage.Storage;

import static org.slf4j.LoggerFactory.getLogger;

public class Runtime {

    public static class Builder {

        public Builder() {
            injectorBuilder = new Injector.Builder();
            runtime = new Runtime();

            injectorBuilder.registerInjectableService(runtime);
            injectorBuilder.registerInjectableService(runtime.configuration);
            injectorBuilder.registerInjectableService(runtime.transactionRunnerLocator);

            environmentBuilder = new EnvironmentBuilder()
                .injectorBuilder(injectorBuilder)
                .transactionRunnerLocator(runtime.transactionRunnerLocator);
        }

        private Injector.Builder injectorBuilder = new Injector.Builder();

        private Runtime runtime;

        public Builder withBoundedContexts(Collection<BoundedContext> boundedContexts) {
            boundedContexts.forEach(this::withBoundedContext);
            return this;
        }

        public Builder withBoundedContext(BoundedContext boundedContext) {
            environmentBuilder.withBoundedContext(boundedContext);
            return this;
        }

        private EnvironmentBuilder environmentBuilder;

        public Builder withConfiguration(String key, Object value) {
            runtime.configuration.add(key, value);
            return this;
        }

        public Builder failFast(boolean failFast) {
            runtime.failFast = failFast;
            return this;
        }

        public Builder withInjectableService(Object service) {
            injectorBuilder.registerInjectableService(service);
            return this;
        }

        public Builder withInjectableService(Class<?> serviceClass, Object service) {
            injectorBuilder.registerInjectableService(serviceClass, service);
            return this;
        }

        public Builder messageConsumptionHandler(MessageConsumptionHandler messageConsumptionHandler) {
            runtime.messageConsumptionHandler = messageConsumptionHandler;
            return this;
        }

        public Builder processingThreads(int processingThreads) {
            this.processingThreads = processingThreads;
            return this;
        }

        private int processingThreads = 1;

        public Runtime buildAndStart() {
            Runtime builtRuntime = build();
            builtRuntime.start();
            return builtRuntime;
        }

        public Runtime build() {
            runtime.environment = environmentBuilder.build();
            runtime.transactionRunnerLocator.setEnvironment(runtime.environment);
            load();
            return runtime;
        }

        private void load() {
            configureContext();
            injectDependencies();
        }

        private void configureContext() {
            configureMessageSenderLocator();
            configureMessageProcessing();
            configureMessageEmissionPolicies();
            runtime.injector = injectorBuilder.build();
        }

        private void configureMessageProcessing() {
            runtime.messageProcessingThreadPool = new MessageProcessingThreadPool.Builder()
                    .numberOfThreads(processingThreads)
                    .failFast(runtime.failFast)
                    .messageConsumptionHandler(runtime.messageConsumptionHandler)
                    .listenersSet(runtime.environment.messageListenersSet())
                    .build();
            runtime.messageBroker = new MessageBroker(runtime.messageProcessingThreadPool);
        }

        private void configureMessageSenderLocator() {
            runtime.messageSenderLocator = new MessageSenderLocator.Builder()
                    .environment(runtime.environment)
                    .connections(runtime.connections)
                    .build();
        }

        private void configureMessageEmissionPolicies() {
            for (Storage storage : runtime.environment.storages()) {
                storage.getMessageSendingPolicy().setMessageSenderLocator(runtime.messageSenderLocator);
            }
        }

        private void injectDependencies() {
            runtime.environment.injectionCandidates().forEach(runtime.injector::injectDependenciesInto);
            runtime.injector.injectDependenciesInto(runtime.messageConsumptionHandler);
        }
    }

    private Runtime() {

    }

    private Configuration configuration = new Configuration();

    public Configuration configuration() {
        return configuration;
    }

    private Environment environment;

    public Environment environment() {
        return environment;
    }

    private Injector injector;

    public Injector injector() {
        return injector;
    }

    private TransactionRunnerLocator transactionRunnerLocator = new TransactionRunnerLocator();

    private boolean failFast;

    private MessageConsumptionHandler messageConsumptionHandler = new DefaultConsumptionHandler();

    private MessageBroker messageBroker;

    private MessageProcessingThreadPool messageProcessingThreadPool;

    private MessageSenderLocator messageSenderLocator;

    MessageSenderLocator messageSenderLocator() {
        return messageSenderLocator;
    }

    public synchronized void start() {
        if(started) {
            return;
        }
        started = true;
        messageProcessingThreadPool.start();
        startMessageHandling();
    }

    private boolean started;

    private List<MessagingConnection> connections = new ArrayList<>();

    private void startMessageHandling() {
        logger.info("Starting message handling...");
        connectMessaging();
        for(MessagingConnection connection : connections) {
            connection.startReceiving();
        }
    }

    private Logger logger = getLogger(getClass());

    private void connectMessaging() {
        for(Messaging messaging : environment.messagings()) {
            connections.add(messaging.connect(messageBroker));
        }
    }

    public synchronized void stop() {
        if(!started) {
            return;
        }
        started = false;
        stopMessageHandling();
        messageProcessingThreadPool.stop();
    }

    private void stopMessageHandling() {
        for(MessagingConnection connection : connections) {
            connection.stopReceiving();
        }
    }

    public synchronized void registerListenersOf(Object service) {
        CustomMessageListenerDiscoverer explorer = new CustomMessageListenerDiscoverer.Builder()
                .service(service)
                .build();
        explorer.discoverListeners().stream()
            .forEach(environment::registerMessageListener);
        if(started) {
            logger.warn("New listeners registered, creating new processing thread pool. Consider registering all listeners before starting runtime to prevent this.");
            var newThreadPool = new MessageProcessingThreadPool.Builder()
                    .numberOfThreads(messageProcessingThreadPool.size())
                    .failFast(failFast)
                    .messageConsumptionHandler(messageConsumptionHandler)
                    .listenersSet(environment.messageListenersSet())
                    .build();
            messageBroker.replaceThreadPool(newThreadPool);
            newThreadPool.start();
        }
    }

    public List<MessagingConnection> messagingConnections() {
        return Collections.unmodifiableList(connections);
    }

    public <C extends Command> C newCommand(Class<C> commandClass) {
        return environment.messageFactory().newMessage(commandClass);
    }

    public void submitCommand(Command command) {
        messageSenderLocator.locate(command.getClass()).sendMessage(command);
    }

    public <D extends DomainEvent> D newDomainEvent(Class<D> eventClass) {
        return environment.messageFactory().newMessage(eventClass);
    }

    public void emitDomainEvent(DomainEvent event) {
        messageSenderLocator.locate(event.getClass()).sendMessage(event);
    }
}
