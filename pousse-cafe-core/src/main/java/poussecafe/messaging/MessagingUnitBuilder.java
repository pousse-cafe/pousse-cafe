package poussecafe.messaging;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.util.stream.Collectors.toList;
import static poussecafe.check.Checks.checkThatValue;

public class MessagingUnitBuilder {

    MessagingUnitBuilder(Messaging messaging) {
        checkThatValue(messaging).notNull();
        this.messaging = messaging;
    }

    private Messaging messaging;

    @SuppressWarnings({ "unchecked" })
    public MessagingUnitBuilder withPackage(String packageName) {
        Reflections reflections = new Reflections(packageName);

        Set<Class<?>> implementationClasses = reflections.getTypesAnnotatedWith(MessageImplementation.class);
        for(Class<?> messageImplementationClass : implementationClasses) {
            if(messaging.nameIn(messageImplementationClass.getAnnotation(MessageImplementation.class).messagingNames())) {
                logger.debug("Adding message implementation {}", messageImplementationClass);
                messageImplementationClasses.add((Class<Message>) messageImplementationClass);
            }
        }
        return this;
    }

    private Logger logger = LoggerFactory.getLogger(getClass());

    private Set<Class<Message>> messageImplementationClasses = new HashSet<>();

    public MessagingUnit build() {
        MessagingUnit unit = new MessagingUnit();
        unit.messaging = messaging;
        unit.implementations = new ArrayList<>();

        List<poussecafe.messaging.MessageImplementationConfiguration> nonRootEntityImplementations = messageImplementationClasses.stream()
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
