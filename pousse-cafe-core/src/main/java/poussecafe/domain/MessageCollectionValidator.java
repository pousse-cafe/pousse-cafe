package poussecafe.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import poussecafe.environment.ExpectedEvent;
import poussecafe.messaging.Message;
import poussecafe.storage.MessageCollection;

import static java.util.stream.Collectors.toSet;

public class MessageCollectionValidator {

    public List<Error> validate(MessageCollection messageCollection) {
        List<Message> messages = messageCollection.getMessages();
        var errors = new ArrayList<Error>();
        Set<Message> messagesSet = new HashSet<>(messages);
        if(messages.size() != messagesSet.size()) {
            errors.add(Error.duplicate());
        }
        errors.addAll(checkProducedEvents(messageCollection));
        return errors;
    }

    public void addExpectedEvent(ExpectedEvent event) {
        expectedEvents.add(event);
    }

    private List<ExpectedEvent> expectedEvents = new ArrayList<>();

    private List<String> listenerIds = new ArrayList<>();

    public List<String> listenerIds() {
        return Collections.unmodifiableList(listenerIds);
    }

    private List<Error> checkProducedEvents(MessageCollection messageCollection) {
        var errors = new ArrayList<Error>();
        Set<ExpectedEvent> requiredEvents = expectedEvents.stream()
                .filter(ExpectedEvent::required)
                .collect(toSet());
        for(Message producedEvent : messageCollection.getMessages()) {
            Optional<ExpectedEvent> match = expectedEvents.stream()
                    .filter(expectedEvent -> expectedEvent.matches((DomainEvent) producedEvent))
                    .findFirst();
            if(match.isPresent()) {
                requiredEvents.remove(match.get());
            } else {
                errors.add(Error.unexpected(producedEvent.getClass()));
            }
        }
        for(ExpectedEvent event : requiredEvents) {
            errors.add(Error.missing(event.producedEventClass()));
        }
        return errors;
    }

    public static class Error {

        private static Error unexpected(Class<? extends Message> messageClass) {
            var error = new Error();
            error.type = ErrorType.UNEXPECTED;
            error.messageClass = messageClass;
            return error;
        }

        private Error() {

        }

        private static Error missing(Class<? extends Message> messageClass) {
            var error = new Error();
            error.type = ErrorType.MISSING;
            error.messageClass = messageClass;
            return error;
        }

        public static Error duplicate() {
            var error = new Error();
            error.type = ErrorType.DUPLICATE;
            return error;
        }

        public enum ErrorType {
            DUPLICATE,
            UNEXPECTED,
            MISSING
        }

        private ErrorType type;

        public ErrorType type() {
            return type;
        }

        private Class<? extends Message> messageClass;

        public Optional<Class<? extends Message>> messageClass() {
            return Optional.ofNullable(messageClass);
        }
    }

    public void addListenerId(String listenerId) {
        listenerIds.add(listenerId);
    }

    public void addExpectedEvents(List<ExpectedEvent> events) {
        expectedEvents.addAll(events);
    }
}
