package poussecafe.domain;

import java.util.List;
import poussecafe.domain.MessageCollectionValidator.Error;
import poussecafe.domain.MessageCollectionValidator.Error.ErrorType;
import poussecafe.environment.ExpectedEvent;
import poussecafe.runtime.ActiveAggregate;

import static java.util.stream.Collectors.joining;

public abstract class AggregateRoot<K, D extends EntityAttributes<K>> extends Entity<K, D> {

    public void onAdd() {

    }

    public void onUpdate() {

    }

    public void onDelete() {

    }

    @Override
    public D attributes() {
        ActiveAggregate.instance().set(this);
        return super.attributes();
    }

    void validateIssuedMessages() {
        var errors = messageCollectionValidator.validate(messageCollection());
        if(!errors.isEmpty()) {
            throw new IllegalStateException(buildExceptionMessage(errors));
        }
    }

    private String buildExceptionMessage(List<Error> errors) {
        var builder = new StringBuilder();
        builder.append("Invalid collection of issued events following the execution of listeners ");
        builder.append(messageCollectionValidator.listenerIds().stream()
                .collect(joining(", ")));
        builder.append(": ");
        builder.append(errors.stream()
                .map(this::errorMessage)
                .collect(joining(", ")));
        return builder.toString();
    }

    private String errorMessage(Error error) {
        if(error.type() == ErrorType.DUPLICATE) {
            return "the same message has been sent more than once";
        } else if(error.type() == ErrorType.UNEXPECTED) {
            return "issued event with class " + error.messageClass().orElseThrow().getCanonicalName()
                    + " does not match any expected event";
        } else if(error.type() == ErrorType.MISSING) {
            return "required event " + error.messageClass().orElseThrow().getCanonicalName() + " was not issued";
        } else {
            throw new UnsupportedOperationException();
        }
    }

    private MessageCollectionValidator messageCollectionValidator = new MessageCollectionValidator();

    public void addExecutedListener(String listenerId) {
        messageCollectionValidator.addListenerId(listenerId);
    }

    public void addExpectedEvents(List<ExpectedEvent> expectedEvents) {
        messageCollectionValidator.addExpectedEvents(expectedEvents);
    }
}
