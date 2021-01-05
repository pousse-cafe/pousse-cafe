package poussecafe.source.validation.listener;

import poussecafe.source.analysis.CompilationUnitResolver;
import poussecafe.source.analysis.ResolvedClass;
import poussecafe.source.model.MessageListenerContainerType;
import poussecafe.source.validation.SubValidator;
import poussecafe.source.validation.ValidationMessage;
import poussecafe.source.validation.ValidationMessageType;
import poussecafe.source.validation.model.MessageListener;
import poussecafe.source.validation.model.Runner;
import poussecafe.source.validation.model.ValidationModel;

public class MessageListenerValidator extends SubValidator {

    @Override
    public void validate() {
        for(MessageListener listener : model.messageListeners()) {
            validateListener(listener);
        }
    }

    @Override
    protected String name() {
        return "Listeners";
    }

    private void validateListener(MessageListener listener) {
        if(!listener.isPublic()) {
            messages.add(new ValidationMessage.Builder()
                    .location(listener.sourceFileLine())
                    .type(ValidationMessageType.ERROR)
                    .message("A message listener must be public")
                    .build());
        }

        if(listener.parametersCount() != 1) {
            messages.add(new ValidationMessage.Builder()
                    .location(listener.sourceFileLine())
                    .type(ValidationMessageType.ERROR)
                    .message("A message listener must have a single parameter")
                    .build());
        }

        if(listener.consumedMessageClass().isEmpty()
                || !isMessage(listener.consumedMessageClass().orElseThrow())) {
            messages.add(new ValidationMessage.Builder()
                    .location(listener.sourceFileLine())
                    .type(ValidationMessageType.ERROR)
                    .message("A message listener must consume a message i.e. have a subclass of Message as single parameter's type")
                    .build());
        }

        if(listener.containerType() != MessageListenerContainerType.FACTORY
                && listener.returnsValue()) {
            messages.add(new ValidationMessage.Builder()
                    .location(listener.sourceFileLine())
                    .type(ValidationMessageType.WARNING)
                    .message("Only message listeners defined in factories should return a value")
                    .build());
        }

        if(listener.containerType() == MessageListenerContainerType.ROOT
                && listener.runnerQualifiedClassName().isEmpty()) {
            messages.add(new ValidationMessage.Builder()
                    .location(listener.sourceFileLine())
                    .type(ValidationMessageType.ERROR)
                    .message("An aggregate root message listener must have a runner")
                    .build());
        }

        if(listener.containerType() != MessageListenerContainerType.ROOT
                && listener.runnerQualifiedClassName().isPresent()) {
            messages.add(new ValidationMessage.Builder()
                    .location(listener.sourceFileLine())
                    .type(ValidationMessageType.WARNING)
                    .message("Only aggregate root message listeners need a runner")
                    .build());
        }

        if(canValidateRunner(listener)) {
            validateListenerRunner(listener);
        }
    }

    private boolean isMessage(ResolvedClass consumedMessageClass) {
        try {
            return consumedMessageClass.instanceOf(CompilationUnitResolver.MESSAGE_CLASS);
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    private boolean canValidateRunner(MessageListener listener) {
        return listener.containerType() == MessageListenerContainerType.ROOT
                && listener.runnerQualifiedClassName().isPresent()
                && listener.consumedMessageClass().isPresent()
                && isMessage(listener.consumedMessageClass().orElseThrow());
    }

    private void validateListenerRunner(MessageListener listener) {
        var runnerClassQualifiedName = listener.runnerQualifiedClassName().orElseThrow();
        var runner = model.runner(runnerClassQualifiedName);
        if(runner.isEmpty()) {
            messages.add(new ValidationMessage.Builder()
                    .location(listener.sourceFileLine())
                    .type(ValidationMessageType.ERROR)
                    .message("Runner class does not implement AggregateMessageListenerRunner interface or is not concrete")
                    .build());
        } else {
            validateConcreteRunner(listener, runner.get());
        }
    }

    private void validateConcreteRunner(MessageListener listener, Runner runner) {
        var messageDefinitionQualifiedName = listener.consumedMessageClass().orElseThrow().name().qualified();
        if(!runner.typeParametersQualifiedNames().contains(messageDefinitionQualifiedName)) {
            messages.add(new ValidationMessage.Builder()
                    .location(runner.sourceFileLine())
                    .type(ValidationMessageType.WARNING)
                    .message("Runner does not handle listener's consumed message")
                    .build());
        }
    }

    public MessageListenerValidator(ValidationModel model) {
        super(model);
    }
}
