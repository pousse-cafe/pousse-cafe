package poussecafe.discovery;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import poussecafe.domain.AggregateFactory;
import poussecafe.domain.AggregateRepository;
import poussecafe.domain.AggregateRoot;
import poussecafe.environment.AggregateMessageListenerRunner;
import poussecafe.environment.ExpectedEvent;
import poussecafe.environment.MessageListenerDefinition;
import poussecafe.exception.PousseCafeException;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
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

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void configureExpectedEvents(Class<?> containerClass, Method method, MessageListenerDefinition.Builder definitionBuilder) {
        List<ProducesEvent> producesEventAnnotations = producesEventAnnotationsOf(method);
        if(AggregateFactory.class.isAssignableFrom(containerClass)) {
            producesEventAnnotations.addAll(producesEventAnnotationsOfAggregateRootOnAdd((Class<? extends AggregateFactory>) containerClass));
        }
        boolean withExpectedEvents = !producesEventAnnotations.isEmpty();
        if(withExpectedEvents) {
            if(isAggregateRootOrAggregateService(containerClass)) {
                List<ExpectedEvent> expectedEvents = producesEventAnnotations.stream()
                        .map(this::expectedEvent)
                        .collect(toList());
                definitionBuilder.withExpectedEvents(true);
                definitionBuilder.expectedEvents(expectedEvents);
            } else {
                throw new PousseCafeException(ProducesEvent.class.getSimpleName() + " annotation is only allowed on AggregateRoot, Factory or Repository instances");
            }
        }
    }

    private List<ProducesEvent> producesEventAnnotationsOf(Method method) {
        List<ProducesEvent> producesEventAnnotations = new ArrayList<>();
        ProducesEvents producesEventsAnnotation = method.getAnnotation(ProducesEvents.class);
        if(producesEventsAnnotation != null) {
            producesEventAnnotations.addAll(asList(producesEventsAnnotation.value()));
        } else {
            ProducesEvent producesEventAnnotation = method.getAnnotation(ProducesEvent.class);
            if(producesEventAnnotation != null) {
                producesEventAnnotations.add(producesEventAnnotation);
            }
        }
        return producesEventAnnotations;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private List<ProducesEvent> producesEventAnnotationsOfAggregateRootOnAdd(Class<? extends AggregateFactory> factoryClass) {
        ParameterizedType factoryParametrizedType = (ParameterizedType) factoryClass.getGenericSuperclass();
        Class<? extends AggregateFactory> aggregateRootClass = (Class<? extends AggregateFactory>) factoryParametrizedType.getActualTypeArguments()[1];
        try {
            Method onAddMethod = aggregateRootClass.getDeclaredMethod("onAdd");
            return producesEventAnnotationsOf(onAddMethod);
        } catch (NoSuchMethodException | SecurityException e) {
            return emptyList();
        }
    }

    private boolean isAggregateRootOrAggregateService(Class<?> containerClass) {
        return (AggregateRoot.class.isAssignableFrom(containerClass)
                || AggregateFactory.class.isAssignableFrom(containerClass)
                || AggregateRepository.class.isAssignableFrom(containerClass));
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
                .producedEventClass(annotation.value())
                .required(annotation.required())
                .build();
    }
}
