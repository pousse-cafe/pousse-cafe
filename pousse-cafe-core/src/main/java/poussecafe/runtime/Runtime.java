package poussecafe.runtime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.slf4j.Logger;
import poussecafe.apm.ApplicationPerformanceMonitoring;
import poussecafe.apm.DefaultApplicationPerformanceMonitoring;
import poussecafe.discovery.CustomMessageListenerDiscoverer;
import poussecafe.domain.DomainEvent;
import poussecafe.environment.Bundle;
import poussecafe.environment.Environment;
import poussecafe.environment.EnvironmentBuilder;
import poussecafe.injector.Injector;
import poussecafe.messaging.MessageReceiverConfiguration;
import poussecafe.messaging.Messaging;
import poussecafe.messaging.MessagingConnection;
import poussecafe.processing.MessageBroker;
import poussecafe.processing.MessageConsumptionConfiguration;
import poussecafe.processing.MessageProcessingThreadPool;
import poussecafe.processing.ReceivedMessage;
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

        public Builder withBundles(Bundles bundles) {
            bundles.forEach(this::withBundle);
            return this;
        }

        public Builder withBundle(Bundle bundle) {
            environmentBuilder.withBundle(bundle);
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

        public Builder failOnDeserializationError(boolean failOnDeserializationError) {
            runtime.failOnDeserializationError = failOnDeserializationError;
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

        public Builder applicationPerformanceMonitoring(ApplicationPerformanceMonitoring applicationPerformanceMonitoring) {
            runtime.applicationPerformanceMonitoring = applicationPerformanceMonitoring;
            return this;
        }

        public Builder processingThreads(int processingThreads) {
            runtime.processingThreads = processingThreads;
            return this;
        }

        public Builder messageConsumptionConfiguration(MessageConsumptionConfiguration messageConsumptionConfiguration) {
            runtime.messageConsumptionConfiguration = messageConsumptionConfiguration;
            return this;
        }

        public Runtime buildAndStart() {
            Runtime builtRuntime = build();
            builtRuntime.start();
            return builtRuntime;
        }

        public Runtime build() {
            environmentBuilder.applicationPerformanceMonitoring(runtime.applicationPerformanceMonitoring);
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
            configureMessageEmissionPolicies();
            runtime.injector = injectorBuilder.build();
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

    private ApplicationPerformanceMonitoring applicationPerformanceMonitoring = new DefaultApplicationPerformanceMonitoring();

    MessageSenderLocator messageSenderLocator() {
        return messageSenderLocator;
    }

    public synchronized void start() {
        if(started) {
            return;
        }
        started = true;
        configureMessageProcessing();
        messageProcessingThreadPool.start();
        openConnections();
    }

    private boolean started;

    private void configureMessageProcessing() {
        logger.info("Creating message processing pool with {} threads...", processingThreads);
        messageProcessingThreadPool = newMessageProcessingThreadPool();
        messageBroker = new MessageBroker(messageProcessingThreadPool);
    }

    private MessageProcessingThreadPool newMessageProcessingThreadPool() {
        return new MessageProcessingThreadPool.Builder()
                .numberOfThreads(processingThreads)
                .failFast(failFast)
                .messageConsumptionHandler(messageConsumptionHandler)
                .applicationPerformanceMonitoring(applicationPerformanceMonitoring)
                .messageConsumptionConfiguration(messageConsumptionConfiguration)
                .listenersSet(environment.messageListenersSet())
                .build();
    }

    private int processingThreads = 1;

    private MessageConsumptionConfiguration messageConsumptionConfiguration = new MessageConsumptionConfiguration.Builder()
            .backOffCeiling(10)
            .backOffSlotTime(3.0)
            .maxConsumptionRetries(50)
            .build();

    private List<MessagingConnection> connections = new ArrayList<>();

    private void openConnections() {
        logger.info("Open connections...");
        connectMessaging();
        for(MessagingConnection connection : connections) {
            connection.open();
        }
    }

    private Logger logger = getLogger(getClass());

    private void connectMessaging() {
        for(Messaging messaging : environment.messagings()) {
            connections.add(messaging.connect(new MessageReceiverConfiguration.Builder()
                    .messageBroker(messageBroker)
                    .failOnDeserializationError(failOnDeserializationError)
                    .build()));
        }
    }

    private boolean failOnDeserializationError;

    public synchronized void stop() {
        if(!started) {
            return;
        }
        started = false;
        closeConnections();
        messageProcessingThreadPool.stop();
    }

    private void closeConnections() {
        for(MessagingConnection connection : connections) {
            connection.close();
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
            var newThreadPool = newMessageProcessingThreadPool();
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

    public CompletableFuture<Void> submitCommand(Command command) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        messageBroker.dispatch(new ReceivedMessage.Builder()
                .payload(new OriginalAndMarshaledMessage.Builder()
                        .marshaled(command)
                        .original(command)
                        .build())
                .acker(() -> future.complete(null))
                .interrupter(() -> future.complete(null))
                .build());
        return future;
    }

    public void sendCommand(Command command) {
        messageSenderLocator.locate(command.getClass()).sendMessage(command);
    }

    public <D extends DomainEvent> D newDomainEvent(Class<D> eventClass) {
        return environment.messageFactory().newMessage(eventClass);
    }

    public void emitDomainEvent(DomainEvent event) {
        messageSenderLocator.locate(event.getClass()).sendMessage(event);
    }

    boolean hasThreadPool() {
        return messageProcessingThreadPool != null;
    }
}
