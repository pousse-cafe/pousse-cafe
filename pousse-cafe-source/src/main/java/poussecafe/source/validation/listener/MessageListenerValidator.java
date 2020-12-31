package poussecafe.source.validation.listener;

import poussecafe.source.model.MessageListenerContainerType;
import poussecafe.source.validation.SubValidator;
import poussecafe.source.validation.ValidationMessage;
import poussecafe.source.validation.ValidationMessageType;
import poussecafe.source.validation.model.MessageListener;
import poussecafe.source.validation.model.ValidationModel;

public class MessageListenerValidator extends SubValidator {

    @Override
    public void validate() {
        for(MessageListener listener : model.messageListeners()) {
            validateListener(listener);
        }
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

        if(listener.consumedMessageQualifiedClassName().isEmpty()
                || !model.hasMessageDefinition(listener.consumedMessageQualifiedClassName().orElseThrow())) {
            messages.add(new ValidationMessage.Builder()
                    .location(listener.sourceFileLine())
                    .type(ValidationMessageType.ERROR)
                    .message("A message listener must consume a message i.e. have a message definition type as single parameter's type")
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
    }

    public MessageListenerValidator(ValidationModel model) {
        super(model);
    }
}
