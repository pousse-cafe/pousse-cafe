package poussecafe.source.validation.entity;

import java.util.HashMap;
import poussecafe.source.validation.SubValidator;
import poussecafe.source.validation.ValidationMessage;
import poussecafe.source.validation.ValidationMessageType;
import poussecafe.source.validation.model.EntityDefinition;
import poussecafe.source.validation.model.EntityImplementation;
import poussecafe.source.validation.model.ValidationModel;

public class EntityValidator extends SubValidator {

    public EntityValidator(ValidationModel model) {
        super(model);
    }

    @Override
    public void validate() {
        var entiyValidations = buildMessageValidationModels();
        for(EntityValidationModel entiyValidationModel : entiyValidations.values()) {
            applyConflictingMessageDefinitionsValidation(entiyValidationModel);
            applyNoMessageImplementationValidation(entiyValidationModel);
            applyConflictingMessageImplementationsValidation(entiyValidationModel);
        }
    }

    private HashMap<String, EntityValidationModel> buildMessageValidationModels() {
        var entiyValidations = new HashMap<String, EntityValidationModel>();
        for(EntityDefinition definition : model.entityDefinitions()) {
            var entiyIdentifier = definition.qualifiedClassName();
            var entiyValidationModel = entiyValidations.computeIfAbsent(entiyIdentifier,
                    EntityValidationModel::new);
            entiyValidationModel.includeDefinition(definition);
        }
        for(EntityImplementation implementation : model.entityImplementations()) {
            var entiyIdentifier = implementation.entityDefinitionQualifiedClassName();
            if(entiyIdentifier.isPresent()) {
                var entiyValidationModel = entiyValidations.computeIfAbsent(entiyIdentifier.get(),
                        EntityValidationModel::new);
                entiyValidationModel.includeImplementation(implementation);
            } else {
                messages.add(new ValidationMessage.Builder()
                        .location(implementation.sourceFileLine())
                        .type(ValidationMessageType.WARNING)
                        .message("Entity implementation is not linked to a definition, add @DataImplementation annotation")
                        .build());
            }
        }
        return entiyValidations;
    }

    private void applyConflictingMessageDefinitionsValidation(EntityValidationModel entiyValidationModel) {
        if(entiyValidationModel.hasConflictingDefinitions()) {
            for(EntityDefinition definition : entiyValidationModel.definitions()) {
                messages.add(new ValidationMessage.Builder()
                        .location(definition.sourceFileLine())
                        .type(ValidationMessageType.ERROR)
                        .message("Conflicting definitions for entity " + entiyValidationModel.entityIdentifier() + ", make implementations mutually exclusive w.r.t. entity name")
                        .build());
            }
        }
    }

    private void applyNoMessageImplementationValidation(EntityValidationModel entiyValidationModel) {
        if(entiyValidationModel.hasNoImplementation()) {
            for(EntityDefinition definition : entiyValidationModel.definitions()) {
                messages.add(new ValidationMessage.Builder()
                        .location(definition.sourceFileLine())
                        .type(ValidationMessageType.WARNING)
                        .message("No implementation found for entiy " + entiyValidationModel.entityIdentifier())
                        .build());
            }
        }
    }

    private void applyConflictingMessageImplementationsValidation(EntityValidationModel entiyValidationModel) {
        if(entiyValidationModel.hasConflictingImplementations()) {
            for(EntityImplementation implementation : entiyValidationModel.implementations()) {
                messages.add(new ValidationMessage.Builder()
                        .location(implementation.sourceFileLine())
                        .type(ValidationMessageType.ERROR)
                        .message("Conflicting implementations for entiy " + entiyValidationModel.entityIdentifier())
                        .build());
            }
        }
    }
}
