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
        validateDefinitions();
        validateImplementations();
    }

    private void validateDefinitions() {
        var entiyValidations = buildEntityDefinitionValidationModels();
        for(EntityDefinitionValidationModel entiyValidationModel : entiyValidations.values()) {
            applyConflictingMessageDefinitionsValidation(entiyValidationModel);
            applyNoImplementationValidation(entiyValidationModel);
            applyConflictingMessageImplementationsValidation(entiyValidationModel);
        }
    }

    private HashMap<String, EntityDefinitionValidationModel> buildEntityDefinitionValidationModels() {
        var entiyValidations = new HashMap<String, EntityDefinitionValidationModel>();
        for(EntityDefinition definition : model.entityDefinitions()) {
            var definitionIdentifier = definition.qualifiedClassName();
            var validationModel = entiyValidations.computeIfAbsent(definitionIdentifier,
                    EntityDefinitionValidationModel::new);
            validationModel.includeDefinition(definition);
        }
        for(EntityImplementation implementation : model.entityImplementations()) {
            var entiyIdentifier = implementation.entityDefinitionQualifiedClassName();
            if(entiyIdentifier.isPresent()) {
                var validationModel = entiyValidations.computeIfAbsent(entiyIdentifier.get(),
                        EntityDefinitionValidationModel::new);
                validationModel.includeImplementation(implementation);
            }
        }
        return entiyValidations;
    }

    private void applyConflictingMessageDefinitionsValidation(EntityDefinitionValidationModel entiyValidationModel) {
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

    private void applyNoImplementationValidation(EntityDefinitionValidationModel entiyValidationModel) {
        if(entiyValidationModel.hasNoImplementation()) {
            for(EntityDefinition definition : entiyValidationModel.definitions()) {
                messages.add(new ValidationMessage.Builder()
                        .location(definition.sourceFileLine())
                        .type(ValidationMessageType.WARNING)
                        .message("No implementation found for entity " + entiyValidationModel.entityIdentifier())
                        .build());
            }
        }
    }

    private void applyConflictingMessageImplementationsValidation(EntityDefinitionValidationModel entiyValidationModel) {
        if(entiyValidationModel.hasConflictingImplementations()) {
            for(EntityImplementation implementation : entiyValidationModel.implementations()) {
                messages.add(new ValidationMessage.Builder()
                        .location(implementation.sourceFileLine())
                        .type(ValidationMessageType.ERROR)
                        .message("Conflicting implementations for entity " + entiyValidationModel.entityIdentifier())
                        .build());
            }
        }
    }

    private void validateImplementations() {
        var entiyValidations = buildEntityImplementationValidationModels();
        for(EntityImplementationValidationModel entiyValidationModel : entiyValidations.values()) {
            applyNoDefinitionValidation(entiyValidationModel);
        }
    }

    private HashMap<String, EntityImplementationValidationModel> buildEntityImplementationValidationModels() {
        var validationModels = new HashMap<String, EntityImplementationValidationModel>();

        for(EntityImplementation implementation : model.entityImplementations()) {
            var implementationQualifiedName = implementation.entityImplementationQualifiedClassName();
            if(implementationQualifiedName.isEmpty()) {
                messages.add(new ValidationMessage.Builder()
                        .location(implementation.sourceFileLine())
                        .type(ValidationMessageType.WARNING)
                        .message("Missing @DataAccessImplementation annotation")
                        .build());
            } else {
                var implementationId = EntityImplementationValidationModel.implementationId(implementation);
                var validationModel = validationModels.computeIfAbsent(implementationId,
                        id -> new EntityImplementationValidationModel(implementation));

                var definitionId = implementation.entityDefinitionQualifiedClassName();
                if(definitionId.isPresent()) {
                    validationModel.includeDefinition(definitionId.get());
                }
            }
        }
        return validationModels;
    }

    private void applyNoDefinitionValidation(EntityImplementationValidationModel entiyValidationModel) {
        if(entiyValidationModel.hasNoDefinition()) {
            var implementation = entiyValidationModel.implementation();
            messages.add(new ValidationMessage.Builder()
                    .location(implementation.sourceFileLine())
                    .type(ValidationMessageType.WARNING)
                    .message("Entity implementation is not linked to a definition, add a @DataImplementation annotation or declare a data access implementation annotated with @DataAccessImplementation")
                    .build());
        }
    }
}
