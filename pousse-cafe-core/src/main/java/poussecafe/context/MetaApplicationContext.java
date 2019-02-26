package poussecafe.context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.slf4j.Logger;
import poussecafe.environment.Environment;
import poussecafe.environment.EnvironmentBuilder;
import poussecafe.exception.PousseCafeException;
import poussecafe.injector.Injector;
import poussecafe.messaging.Messaging;
import poussecafe.messaging.MessagingConnection;
import poussecafe.storage.Storage;

import static org.slf4j.LoggerFactory.getLogger;

public class MetaApplicationContext {

    private Configuration configuration;

    private Environment environment;

    private Injector injector;

    private TransactionRunnerLocator transactionRunnerLocator;

    private MessageConsumer messageConsumer;

    private MessageSenderLocator messageSenderLocator;

    private Set<BoundedContext> boundedContexts = new HashSet<>();

    public MetaApplicationContext() {
        configuration = new Configuration();
        transactionRunnerLocator = new TransactionRunnerLocator();
        messageConsumer = new MessageConsumer();
        messageSenderLocator = new MessageSenderLocator(connections);
    }

    public Configuration configuration() {
        return configuration;
    }

    public Environment environment() {
        return environment;
    }

    public void addBoundedContext(BoundedContext boundedContext) {
        Objects.requireNonNull(boundedContext);
        boundedContexts.add(boundedContext);
    }

    public synchronized void addConfiguration(String key, Object value) {
        if(started) {
            throw new PousseCafeException("Cannot add configuration entries after meta-application context has been started");
        }
        configuration.add(key, value);
    }

    public synchronized void start() {
        if(started) {
            return;
        }
        started = true;
        load();
        startMessageHandling();
    }

    private boolean started;

    public synchronized void load() {
        injector = new Injector();
        injector.registerInjectableService(configuration);
        injector.registerInjectableService(transactionRunnerLocator);

        environment = new EnvironmentBuilder()
                .transactionRunnerLocator(transactionRunnerLocator)
                .injector(injector)
                .withBoundedContexts(boundedContexts)
                .build();
        transactionRunnerLocator.setEnvironment(environment);

        configureContext();
        injector.injectDependencies();
    }

    private Logger logger = getLogger(getClass());

    private void configureContext() {
        configureMessageConsumer();
        configureMessageSenderLocator();
        configureMessageEmissionPolicies();
    }

    private void configureMessageConsumer() {
        injector.addInjectionCandidate(messageConsumer);
    }

    private List<MessagingConnection> connections = new ArrayList<>();

    private void configureMessageSenderLocator() {
        injector.addInjectionCandidate(messageSenderLocator);
        injector.registerInjectableService(messageSenderLocator);
    }

    protected void configureMessageEmissionPolicies() {
        for (Storage storage : environment.storages()) {
            storage.getMessageSendingPolicy().setMessageSenderLocator(messageSenderLocator);
        }
    }

    public synchronized void startMessageHandling() {
        logger.info("Starting message handling...");
        connectMessaging();
        for(MessagingConnection connection : connections) {
            connection.startReceiving();
        }
    }

    private void connectMessaging() {
        for(Messaging messaging : environment.messagings()) {
            connections.add(messaging.connect(messageConsumer));
        }
    }

    public synchronized void stopMessageHandling() {
        for(MessagingConnection connection : connections) {
            connection.stopReceiving();
        }
    }

    public synchronized void injectDependencies(Object service) {
        injector.injectDependencies(service);
    }

    public synchronized void registerListeners(Object service) {
        ServiceMessageListenerDiscoverer explorer = new ServiceMessageListenerDiscoverer.Builder()
                .service(service)
                .messageListenerFactory(new DeclaredMessageListenerFactory())
                .build();
        explorer.discoverListeners().stream()
            .forEach(environment::registerMessageListener);
    }

    public MessageSenderLocator getMessageSenderLocator() {
        return messageSenderLocator;
    }

    public List<MessagingConnection> messagingConnections() {
        return Collections.unmodifiableList(connections);
    }
}
