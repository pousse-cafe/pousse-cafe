package poussecafe.discovery;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.Factory;
import poussecafe.domain.Repository;
import poussecafe.environment.AggregateMessageListenerRunner;
import poussecafe.environment.ExpectedEvent;
import poussecafe.environment.MessageListenerDefinition;
import poussecafe.exception.PousseCafeException;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

class MessageListenerDefinitionDiscoverer {

    MessageListenerDefinitionDiscoverer(Class<?> containerClass) {
        this.containerClass = containerClass;
    }

    private Class<?> containerClass;

    public Collection<MessageListenerDefinition> discoverListenersOfClass() {
        List<MessageListenerDefinition> definitions = new ArrayList<>();
        for(Method method : containerClass.getMethods()) {
            MessageListener listenerAnnotation = method.getAnnotation(MessageListener.class);
            if(listenerAnnotation != null) {
                logger.debug("Defining listener for method {} of {}", method, containerClass);
                MessageListenerDefinition.Builder definitionBuilder = new MessageListenerDefinition.Builder()
                        .containerClass(containerClass)
                        .method(method)
                        .customId(customId(listenerAnnotation.id()))
                        .runner(runner(listenerAnnotation.runner()));
                configureExpectedEvents(containerClass, method, definitionBuilder);
                definitions.add(definitionBuilder.build());
            }
        }
        return definitions;
    }

    private Logger logger = LoggerFactory.getLogger(getClass());

    private void configureExpectedEvents(Class<?> containerClass, Method method, MessageListenerDefinition.Builder definitionBuilder) {
        ProducesEvents producesEventsAnnotation = method.getAnnotation(ProducesEvents.class);
        if(producesEventsAnnotation != null) {
            ProducesEvent[] producesEventAnnotations = producesEventsAnnotation.value();
            boolean withExpectedEvents = producesEventAnnotations.length > 0;
            if(withExpectedEvents) {
                if(isAggregateRootOrAggregateService(containerClass)) {
                    List<ExpectedEvent> expectedEvents = asList(producesEventAnnotations).stream()
                            .map(this::expectedEvent)
                            .collect(toList());
                    definitionBuilder.withExpectedEvents(true);
                    definitionBuilder.expectedEvents(expectedEvents);
                } else {
                    throw new PousseCafeException(ProducesEvent.class.getSimpleName() + " annotation is only allowed on AggregateRoot, Factory or Repository instances");
                }
            }
        }
    }

    private boolean isAggregateRootOrAggregateService(Class<?> containerClass) {
        return (AggregateRoot.class.isAssignableFrom(containerClass)
                || Factory.class.isAssignableFrom(containerClass)
                || Repository.class.isAssignableFrom(containerClass));
    }

    private Optional<String> customId(String id) {
        if(id == null || id.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(id);
        }
    }

    private Optional<Class<? extends AggregateMessageListenerRunner<?, ?, ?>>> runner(
            Class<? extends AggregateMessageListenerRunner<?, ?, ?>> runner) {
        if(runner == VoidAggregateMessageListenerRunner.class) {
            return Optional.empty();
        } else {
            return Optional.of(runner);
        }
    }

    private ExpectedEvent expectedEvent(ProducesEvent annotation) {
        return new ExpectedEvent.Builder()
                .producedEventClass(annotation.event())
                .required(annotation.required())
                .build();
    }
}
