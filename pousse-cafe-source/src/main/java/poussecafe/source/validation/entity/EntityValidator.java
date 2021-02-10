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
    protected String name() {
        return "Entities";
    }

    @Override
    public void validate() {
        validateDefinitions();
        validateImplementations();
    }

    private void validateDefinitions() {
        var entiyValidations = buildEntityDefinitionValidationModels();
        for(EntityDefinitionValidationModel entityValidationModel : entiyValidations.values()) {
            applyConflictingMessageDefinitionsValidation(entityValidationModel);
            applyNoImplementationValidation(entityValidationModel);
            applyConflictingMessageImplementationsValidation(entityValidationModel);
            applyAbstractImplementationsValidation(entityValidationModel);
        }
    }

    private HashMap<String, EntityDefinitionValidationModel> buildEntityDefinitionValidationModels() {
        var entiyValidations = new HashMap<String, EntityDefinitionValidationModel>();
        for(EntityDefinition definition : model.entityDefinitions()) {
            var definitionIdentifier = definition.className().qualified();
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

    private void applyConflictingMessageDefinitionsValidation(EntityDefinitionValidationModel entityValidationModel) {
        if(entityValidationModel.hasConflictingDefinitions()) {
            for(EntityDefinition definition : entityValidationModel.definitions()) {
                var sourceFileLine = definition.sourceLine();
                if(sourceFileLine.isPresent()) {
                    messages.add(new ValidationMessage.Builder()
                            .location(sourceFileLine.get())
                            .type(ValidationMessageType.ERROR)
                            .message("Conflicting definitions for entity " + entityValidationModel.entityIdentifier() + ", make implementations mutually exclusive w.r.t. entity name")
                            .build());
                }
            }
        }
    }

    private void applyNoImplementationValidation(EntityDefinitionValidationModel entityValidationModel) {
        if(entityValidationModel.hasNoImplementation()) {
            for(EntityDefinition definition : entityValidationModel.definitions()) {
                var sourceFileLine = definition.sourceLine();
                if(sourceFileLine.isPresent()) {
                    messages.add(new ValidationMessage.Builder()
                            .location(sourceFileLine.get())
                            .type(ValidationMessageType.WARNING)
                            .message("No implementation found for entity " + entityValidationModel.entityIdentifier())
                            .build());
                }
            }
        }
    }

    private void applyConflictingMessageImplementationsValidation(EntityDefinitionValidationModel entityValidationModel) {
        if(entityValidationModel.hasConflictingImplementations()) {
            for(EntityImplementation implementation : entityValidationModel.implementations()) {
                var sourceFileLine = implementation.sourceLine();
                if(sourceFileLine.isPresent()) {
                    messages.add(new ValidationMessage.Builder()
                            .location(sourceFileLine.get())
                            .type(ValidationMessageType.ERROR)
                            .message("Conflicting implementations for entity " + entityValidationModel.entityIdentifier())
                            .build());
                }
            }
        }
    }

    private void validateImplementations() {
        var entiyValidations = buildEntityImplementationValidationModels();
        for(EntityImplementationValidationModel entityValidationModel : entiyValidations.values()) {
            applyNoDefinitionValidation(entityValidationModel);
        }
    }

    private HashMap<String, EntityImplementationValidationModel> buildEntityImplementationValidationModels() {
        var validationModels = new HashMap<String, EntityImplementationValidationModel>();

        for(EntityImplementation implementation : model.entityImplementations()) {
            var implementationQualifiedName = implementation.entityImplementationQualifiedClassName();
            var sourceFileLine = implementation.sourceLine();
            if(implementationQualifiedName.isEmpty()
                    && sourceFileLine.isPresent()) {
                messages.add(new ValidationMessage.Builder()
                        .location(sourceFileLine.get())
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

    private void applyNoDefinitionValidation(EntityImplementationValidationModel entityValidationModel) {
        if(entityValidationModel.hasNoDefinition()) {
            var implementation = entityValidationModel.implementation();
            var sourceFileLine = implementation.sourceLine();
            if(sourceFileLine.isPresent()) {
                messages.add(new ValidationMessage.Builder()
                        .location(sourceFileLine.get())
                        .type(ValidationMessageType.WARNING)
                        .message("Entity implementation is not linked to a definition, add a @DataImplementation annotation or declare a data access implementation annotated with @DataAccessImplementation")
                        .build());
            }
        }
    }

    private void applyAbstractImplementationsValidation(EntityDefinitionValidationModel entityValidationModel) {
        for(EntityImplementation implementation : entityValidationModel.implementations()) {
            var sourceFileLine = implementation.sourceLine();
            if(sourceFileLine.isPresent()
                    && !implementation.isConcrete()) {
                messages.add(new ValidationMessage.Builder()
                        .location(sourceFileLine.get())
                        .type(ValidationMessageType.ERROR)
                        .message("Entity implementation must be concrete")
                        .build());
            }
        }
    }
}
