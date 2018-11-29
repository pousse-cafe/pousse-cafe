package poussecafe.messaging;

import java.util.ArrayList;
import java.util.List;
import poussecafe.context.ClassPathExplorer;

import static java.util.stream.Collectors.toList;
import static poussecafe.check.Checks.checkThatValue;

public class MessagingUnitBuilder {

    MessagingUnitBuilder(Messaging messaging) {
        checkThatValue(messaging).notNull();
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

        List<poussecafe.messaging.MessageImplementationConfiguration> nonRootEntityImplementations = classPathExplorer.getMessageImplementations(messaging).stream()
                .map(this::buildNonRootEntityImplementation)
                .collect(toList());
        unit.implementations.addAll(nonRootEntityImplementations);

        return unit;
    }

    private poussecafe.messaging.MessageImplementationConfiguration buildNonRootEntityImplementation(Class<Message> entityDataClass) {
        MessageImplementation annotation = entityDataClass.getAnnotation(MessageImplementation.class);
        return new poussecafe.messaging.MessageImplementationConfiguration.Builder()
                .withMessageClass(annotation.message())
                .withMessageImplementationClass(entityDataClass)
                .withMessaging(messaging)
                .build();
    }
}
