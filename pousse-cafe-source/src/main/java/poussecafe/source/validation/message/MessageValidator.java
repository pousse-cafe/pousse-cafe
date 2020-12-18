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
    public void validate() {
        var messageValidations = buildMessageValidationModels();
        for(MessageValidationModel messageValidationModel : messageValidations.values()) {
            applyConflictingMessageDefinitionsValidation(messageValidationModel);
            applyNoMessageImplementationValidation(messageValidationModel);
            applyConflictingMessageImplementationsValidation(messageValidationModel);
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
            var messageIdentifier = implementation.messageDefinitionQualifiedClassName();
            if(messageIdentifier.isPresent()) {
                var messageValidationModel = messageValidations.computeIfAbsent(messageIdentifier.get(),
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

    private void applyConflictingMessageDefinitionsValidation(MessageValidationModel messageValidationModel) {
        if(messageValidationModel.hasConflictingDefinitions()) {
            for(MessageDefinition definition : messageValidationModel.definitions()) {
                messages.add(new ValidationMessage.Builder()
                        .location(definition.sourceFileLine())
                        .type(ValidationMessageType.ERROR)
                        .message("Conflicting definitions for message " + messageValidationModel.messageIdentifier() + ", make implementations mutually exclusive w.r.t. messaging name")
                        .build());
            }
        }
    }

    private void applyNoMessageImplementationValidation(MessageValidationModel messageValidationModel) {
        if(messageValidationModel.hasNoImplementation()) {
            for(MessageDefinition definition : messageValidationModel.definitions()) {
                messages.add(new ValidationMessage.Builder()
                        .location(definition.sourceFileLine())
                        .type(ValidationMessageType.WARNING)
                        .message("No implementation found for message " + messageValidationModel.messageIdentifier())
                        .build());
            }
        }
    }

    private void applyConflictingMessageImplementationsValidation(MessageValidationModel messageValidationModel) {
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
}
