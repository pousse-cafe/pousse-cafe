package poussecafe.runtime;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import poussecafe.discovery.DeclaredMessageListenerFactory;
import poussecafe.discovery.ServiceMessageListenerDiscoverer;
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
            injectorBuilder = new Injector.Builder();
            runtime = new Runtime();

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
            configureMessageConsumer();
            configureMessageEmissionPolicies();
            runtime.injector = injectorBuilder.build();
        }

        private void configureMessageConsumer() {
            runtime.messageConsumer = new MessageConsumer.Builder()
                    .environment(runtime.environment)
                    .messageSenderLocator(runtime.messageSenderLocator)
                    .build();
        }

        private void configureMessageSenderLocator() {
            runtime.messageSenderLocator = new MessageSenderLocator.Builder()
                    .environment(runtime.environment)
                    .connections(runtime.connections)
                    .build();
            injectorBuilder.registerInjectableService(runtime.messageSenderLocator);
        }

        private void configureMessageEmissionPolicies() {
            for (Storage storage : runtime.environment.storages()) {
                storage.getMessageSendingPolicy().setMessageSenderLocator(runtime.messageSenderLocator);
            }
        }

        private void injectDependencies() {
            runtime.environment.injectionCandidates().forEach(runtime.injector::injectDependenciesInto);
        }
    }

    private Runtime() {

    }

    private Configuration configuration = new Configuration();

    private Environment environment;

    private Injector injector;

    private TransactionRunnerLocator transactionRunnerLocator = new TransactionRunnerLocator();

    private MessageConsumer messageConsumer;

    private MessageSenderLocator messageSenderLocator;

    public Configuration configuration() {
        return configuration;
    }

    public Environment environment() {
        return environment;
    }

    public Injector injector() {
        return injector;
    }

    MessageSenderLocator messageSenderLocator() {
        return messageSenderLocator;
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

    public synchronized void registerListenersOf(Object service) {
        ServiceMessageListenerDiscoverer explorer = new ServiceMessageListenerDiscoverer.Builder()
                .service(service)
                .messageListenerFactory(new DeclaredMessageListenerFactory())
                .build();
        explorer.discoverListeners().stream()
            .forEach(environment::registerMessageListener);
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
}
