package poussecafe.runtime;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import poussecafe.environment.BoundedContext;
import poussecafe.environment.Environment;
import poussecafe.environment.EnvironmentBuilder;
import poussecafe.injector.Injector;
import poussecafe.messaging.Messaging;
import poussecafe.messaging.MessagingConnection;
import poussecafe.storage.Storage;

import static org.slf4j.LoggerFactory.getLogger;

public class Runtime {

    public static class Builder {

        public Builder() {
            runtime = new Runtime();
            environmentBuilder = new EnvironmentBuilder()
                .injector(runtime.injector)
                .transactionRunnerLocator(runtime.transactionRunnerLocator);
        }

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
            runtime.injector.injectDependencies();
        }

        private void configureContext() {
            configureMessageConsumer();
            configureMessageSenderLocator();
            configureMessageEmissionPolicies();
        }

        private void configureMessageConsumer() {
            runtime.injector.addInjectionCandidate(runtime.messageConsumer);
        }

        private void configureMessageSenderLocator() {
            runtime.injector.addInjectionCandidate(runtime.messageSenderLocator);
            runtime.injector.registerInjectableService(runtime.messageSenderLocator);
        }

        private void configureMessageEmissionPolicies() {
            for (Storage storage : runtime.environment.storages()) {
                storage.getMessageSendingPolicy().setMessageSenderLocator(runtime.messageSenderLocator);
            }
        }
    }

    private Runtime() {
        configuration = new Configuration();
        transactionRunnerLocator = new TransactionRunnerLocator();
        messageConsumer = new MessageConsumer();
        messageSenderLocator = new MessageSenderLocator(connections);

        injector = new Injector();
        injector.registerInjectableService(configuration);
        injector.registerInjectableService(transactionRunnerLocator);
    }

    private Configuration configuration;

    private Environment environment;

    private Injector injector;

    private TransactionRunnerLocator transactionRunnerLocator;

    private MessageConsumer messageConsumer;

    private MessageSenderLocator messageSenderLocator;

    public Configuration configuration() {
        return configuration;
    }

    public Environment environment() {
        return environment;
    }

    public synchronized void start() {
        if(started) {
            return;
        }
        started = true;
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
            connections.add(messaging.connect(messageConsumer));
        }
    }

    public synchronized void stop() {
        if(!started) {
            return;
        }
        started = false;
        stopMessageHandling();
    }

    private void stopMessageHandling() {
        for(MessagingConnection connection : connections) {
            connection.stopReceiving();
        }
    }

    public synchronized void injectDependenciesInto(Object service) {
        injector.injectDependencies(service);
    }

    public synchronized void registerListenersOf(Object service) {
        ServiceMessageListenerDiscoverer explorer = new ServiceMessageListenerDiscoverer.Builder()
                .service(service)
                .messageListenerFactory(new DeclaredMessageListenerFactory())
                .build();
        explorer.discoverListeners().stream()
            .forEach(environment::registerMessageListener);
    }

    public MessageSenderLocator messageSenderLocator() {
        return messageSenderLocator;
    }

    public List<MessagingConnection> messagingConnections() {
        return Collections.unmodifiableList(connections);
    }
}
