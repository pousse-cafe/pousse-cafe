package poussecafe.source.validation.listener;

import poussecafe.source.analysis.ClassName;
import poussecafe.source.analysis.ClassResolver;
import poussecafe.source.analysis.CompilationUnitResolver;
import poussecafe.source.validation.SourceLine;
import poussecafe.source.validation.SubValidator;
import poussecafe.source.validation.ValidationMessage;
import poussecafe.source.validation.ValidationMessageType;
import poussecafe.source.validation.model.MessageListener;
import poussecafe.source.validation.model.Runner;
import poussecafe.source.validation.model.ValidationModel;

import static java.util.Objects.requireNonNull;

public class MessageListenerValidator extends SubValidator {

    @Override
    public void validate() {
        for(MessageListener listener : model.messageListeners()) {
            validateListener(listener);
        }
        for(SourceLine line : model.ignoredProducesEventAnnotations()) {
            messages.add(new ValidationMessage.Builder()
                    .location(line)
                    .type(ValidationMessageType.WARNING)
                    .message("ProducesEvent annotations only annotate listeners")
                    .build());
        }
    }

    @Override
    protected String name() {
        return "Listeners";
    }

    private void validateListener(MessageListener listener) {
        if(!listener.isPublic()) {
            messages.add(new ValidationMessage.Builder()
                    .location(listener.sourceLine())
                    .type(ValidationMessageType.ERROR)
                    .message("A message listener must be public")
                    .build());
        }

        if(listener.parametersCount() != 1) {
            messages.add(new ValidationMessage.Builder()
                    .location(listener.sourceLine())
                    .type(ValidationMessageType.ERROR)
                    .message("A message listener must have a single parameter")
                    .build());
        }

        if(listener.consumedMessageClass().isEmpty()
                || !isMessage(listener.consumedMessageClass().orElseThrow())) {
            messages.add(new ValidationMessage.Builder()
                    .location(listener.sourceLine())
                    .type(ValidationMessageType.ERROR)
                    .message("A message listener must consume a message i.e. have a subclass of Message as single parameter's type")
                    .build());
        }

        if(!listener.containerType().isFactory()
                && listener.returnsValue()) {
            messages.add(new ValidationMessage.Builder()
                    .location(listener.sourceLine())
                    .type(ValidationMessageType.WARNING)
                    .message("Only message listeners defined in factories should return a value")
                    .build());
        }

        if(listener.containerType().isRoot()
                && listener.runnerQualifiedClassName().isEmpty()) {
            messages.add(new ValidationMessage.Builder()
                    .location(listener.sourceLine())
                    .type(ValidationMessageType.ERROR)
                    .message("An aggregate root message listener must have a runner")
                    .build());
        }

        if(!listener.containerType().isRoot()
                && listener.runnerQualifiedClassName().isPresent()) {
            messages.add(new ValidationMessage.Builder()
                    .location(listener.sourceLine())
                    .type(ValidationMessageType.WARNING)
                    .message("Only aggregate root message listeners need a runner")
                    .build());
        }

        if(canValidateRunner(listener)) {
            validateListenerRunner(listener);
        }
    }

    private boolean isMessage(ClassName consumedMessageClass) {
        var messageClass = resolver.loadClass(consumedMessageClass);
        if(messageClass.isPresent()) {
            try {
                return messageClass.get().instanceOf(CompilationUnitResolver.MESSAGE_CLASS);
            } catch (ClassNotFoundException e) {
                return false;
            }
        } else {
            return false;
        }
    }

    private ClassResolver resolver;

    private boolean canValidateRunner(MessageListener listener) {
        return listener.containerType().isRoot()
                && listener.runnerQualifiedClassName().isPresent()
                && listener.consumedMessageClass().isPresent()
                && isMessage(listener.consumedMessageClass().orElseThrow());
    }

    private void validateListenerRunner(MessageListener listener) {
        var runnerClassQualifiedName = listener.runnerQualifiedClassName().orElseThrow();
        var runner = model.runner(runnerClassQualifiedName);
        if(runner.isEmpty()) {
            messages.add(new ValidationMessage.Builder()
                    .location(listener.sourceLine())
                    .type(ValidationMessageType.ERROR)
                    .message("Runner class does not implement AggregateMessageListenerRunner interface or is not concrete")
                    .build());
        } else {
            validateConcreteRunner(listener, runner.get());
        }
    }

    private void validateConcreteRunner(MessageListener listener, Runner runner) {
        var messageDefinitionQualifiedName = listener.consumedMessageClass().orElseThrow().qualified();
        if(!runner.typeParametersQualifiedNames().contains(messageDefinitionQualifiedName)) {
            messages.add(new ValidationMessage.Builder()
                    .location(runner.sourceLine().orElseThrow())
                    .type(ValidationMessageType.WARNING)
                    .message("Runner does not handle listener's consumed message")
                    .build());
        }
    }

    public MessageListenerValidator(ValidationModel model, ClassResolver resolver) {
        super(model);

        requireNonNull(resolver);
        this.resolver = resolver;
    }
}
