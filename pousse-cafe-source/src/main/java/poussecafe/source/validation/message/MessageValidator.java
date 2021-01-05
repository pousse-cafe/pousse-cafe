package poussecafe.source.validation.message;

import java.util.HashMap;
import poussecafe.source.validation.SubValidator;
import poussecafe.source.validation.ValidationMessage;
import poussecafe.source.validation.ValidationMessageType;
import poussecafe.source.validation.model.MessageDefinition;
import poussecafe.source.validation.model.MessageImplementation;
import poussecafe.source.validation.model.ValidationModel;

public class MessageValidator extends SubValidator {

    public MessageValidator(ValidationModel model) {
        super(model);
    }

    @Override
    protected String name() {
        return "Messages";
    }

    @Override
    public void validate() {
        var messageValidations = buildMessageValidationModels();
        for(MessageValidationModel messageValidationModel : messageValidations.values()) {
            warnNoDefinition(messageValidationModel);
            errorConflictingMessageDefinitionsValidation(messageValidationModel);
            errorNoMessageImplementationValidation(messageValidationModel);
            errorConflictingMessageImplementationsValidation(messageValidationModel);
            warnAutoImplementedEvent(messageValidationModel);
        }
    }

    private HashMap<String, MessageValidationModel> buildMessageValidationModels() {
        var messageValidations = new HashMap<String, MessageValidationModel>();
        for(MessageDefinition definition : model.messageDefinitions()) {
            var messageIdentifier = definition.qualifiedClassName();
            var messageValidationModel = messageValidations.computeIfAbsent(messageIdentifier,
                    MessageValidationModel::new);
            messageValidationModel.includeDefinition(definition);
        }
        for(MessageImplementation implementation : model.messageImplementations()) {
            var messageIdentifier = implementation.messageDefinitionClassName();
            if(messageIdentifier.isPresent()) {
                var messageValidationModel = messageValidations.computeIfAbsent(messageIdentifier.get().qualified(),
                        MessageValidationModel::new);
                messageValidationModel.includeImplementation(implementation);
            } else {
                messages.add(new ValidationMessage.Builder()
                        .location(implementation.sourceFileLine())
                        .type(ValidationMessageType.WARNING)
                        .message("Message implementation is not linked to a definition, add @MessageImplementation or @AbstractMessage annotation")
                        .build());
            }
        }
        return messageValidations;
    }

    private void warnNoDefinition(MessageValidationModel messageValidationModel) {
        if(messageValidationModel.hasNoDefinition()) {
            for(MessageImplementation implementation : messageValidationModel.implementations()) {
                messages.add(new ValidationMessage.Builder()
                        .location(implementation.sourceFileLine())
                        .type(ValidationMessageType.WARNING)
                        .message("Missing or wrong message definition " + messageValidationModel.messageIdentifier())
                        .build());
            }
        }
    }

    private void errorConflictingMessageDefinitionsValidation(MessageValidationModel messageValidationModel) {
        if(messageValidationModel.hasConflictingDefinitions()) {
            for(MessageDefinition definition : messageValidationModel.definitions()) {
                var sourceFileLine = definition.sourceFileLine();
                if(sourceFileLine.isPresent()) {
                    messages.add(new ValidationMessage.Builder()
                            .location(sourceFileLine.get())
                            .type(ValidationMessageType.ERROR)
                            .message("Conflicting definitions for message " + messageValidationModel.messageIdentifier() + ", make implementations mutually exclusive w.r.t. messaging name")
                            .build());
                }
            }
        }
    }

    private void errorNoMessageImplementationValidation(MessageValidationModel messageValidationModel) {
        if(messageValidationModel.hasNoImplementation()) {
            for(MessageDefinition definition : messageValidationModel.definitions()) {
                var sourceFileLine = definition.sourceFileLine();
                if(sourceFileLine.isPresent()) {
                    messages.add(new ValidationMessage.Builder()
                            .location(sourceFileLine.get())
                            .type(ValidationMessageType.ERROR)
                            .message("No implementation found for message " + messageValidationModel.messageIdentifier())
                            .build());
                }
            }
        }
    }

    private void errorConflictingMessageImplementationsValidation(MessageValidationModel messageValidationModel) {
        if(messageValidationModel.hasConflictingImplementations()) {
            for(MessageImplementation implementation : messageValidationModel.implementations()) {
                messages.add(new ValidationMessage.Builder()
                        .location(implementation.sourceFileLine())
                        .type(ValidationMessageType.ERROR)
                        .message("Conflicting implementations for message " + messageValidationModel.messageIdentifier())
                        .build());
            }
        }
    }

    private void warnAutoImplementedEvent(MessageValidationModel messageValidationModel) {
        if(messageValidationModel.definitions().size() == 1
                && messageValidationModel.implementations().size() == 1) {
            var definition = messageValidationModel.definitions().get(0);
            var implementation = messageValidationModel.implementations().get(0);
            if(definition.isEvent()
                    && implementation.isAutoImplementation()) {
                messages.add(new ValidationMessage.Builder()
                        .location(implementation.sourceFileLine())
                        .type(ValidationMessageType.WARNING)
                        .message("A domain event definition should not implement itself")
                        .build());
            }
        }
    }
}
