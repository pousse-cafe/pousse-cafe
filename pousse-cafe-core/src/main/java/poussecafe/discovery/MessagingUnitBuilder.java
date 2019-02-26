package poussecafe.discovery;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import poussecafe.messaging.Message;
import poussecafe.messaging.Messaging;

import static java.util.stream.Collectors.toList;

public class MessagingUnitBuilder {

    public MessagingUnitBuilder(Messaging messaging) {
        Objects.requireNonNull(messaging);
        this.messaging = messaging;
    }

    private Messaging messaging;

    public MessagingUnitBuilder classPathExplorer(ClassPathExplorer classPathExplorer) {
        this.classPathExplorer = classPathExplorer;
        return this;
    }

    private ClassPathExplorer classPathExplorer;

    public MessagingUnit build() {
        MessagingUnit unit = new MessagingUnit();
        unit.messaging = messaging;
        unit.implementations = new ArrayList<>();

        List<poussecafe.messaging.MessageImplementation> nonRootEntityImplementations = classPathExplorer.getMessageImplementations(messaging).stream()
                .map(this::buildNonRootEntityImplementation)
                .collect(toList());
        unit.implementations.addAll(nonRootEntityImplementations);

        return unit;
    }

    private poussecafe.messaging.MessageImplementation buildNonRootEntityImplementation(Class<Message> entityDataClass) {
        MessageImplementation annotation = entityDataClass.getAnnotation(MessageImplementation.class);
        return new poussecafe.messaging.MessageImplementation.Builder()
                .withMessageClass(annotation.message())
                .withMessageImplementationClass(entityDataClass)
                .withMessaging(messaging)
                .build();
    }
}
