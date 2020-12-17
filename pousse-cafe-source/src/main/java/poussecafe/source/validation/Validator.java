package poussecafe.source.validation;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import poussecafe.source.Source;
import poussecafe.source.SourceConsumer;
import poussecafe.source.SourceScanner;
import poussecafe.source.analysis.ClassLoaderClassResolver;
import poussecafe.source.analysis.ClassResolver;

public class Validator implements SourceConsumer {

    public void validate() {
        model = visitor.buildModel();
        if(result == null) {
            validateMessages();

            result = new ValidationResult(messages);
        }
    }

    private ValidationModel model;

    private ValidationModelBuildingVisitor visitor;

    private void validateMessages() {
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

    private ValidationResult result;

    private List<ValidationMessage> messages = new ArrayList<>();

    public ValidationResult result() {
        return result;
    }

    @Override
    public void includeFile(Path sourceFilePath) throws IOException {
        scanner.includeFile(sourceFilePath);
    }

    private SourceScanner scanner;

    @Override
    public void includeTree(Path sourceDirectory) throws IOException {
        scanner.includeTree(sourceDirectory);
    }

    @Override
    public void includeSource(Source source) {
        scanner.includeSource(source);
    }

    public Validator() {
        this(new ClassLoaderClassResolver());
    }

    public Validator(ClassResolver classResolver) {
        visitor = new ValidationModelBuildingVisitor(classResolver);
        scanner = new SourceScanner(visitor);
    }
}
