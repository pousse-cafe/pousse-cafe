package poussecafe.source.validation.types;

import java.util.ArrayList;
import java.util.List;
import poussecafe.source.analysis.ClassResolver;
import poussecafe.source.generation.AggregatePackage;
import poussecafe.source.generation.NamingConventions;
import poussecafe.source.validation.SourceLine;
import poussecafe.source.validation.SubValidator;
import poussecafe.source.validation.ValidationMessage;
import poussecafe.source.validation.ValidationMessageType;
import poussecafe.source.validation.model.AggregateComponentDefinition;
import poussecafe.source.validation.model.AggregateContainer;
import poussecafe.source.validation.model.ValidationModel;

import static java.util.Objects.requireNonNull;


public class TypesValidator extends SubValidator {

    @Override
    public void validate() {
        for(AggregateComponentDefinition aggregate : model.aggregateRoots()) {
            if(!aggregate.isInnerClass()
                    && aggregate.validClassName()) {
                var aggregatePackage = aggregate.aggregatePackage();
                validateAggregateTypes(aggregate.sourceLine().orElseThrow(), aggregatePackage);
            }
        }
        for(AggregateContainer aggregate : model.aggregateContainers()) {
            var aggregatePackage = aggregate.aggregatePackage();
            validateAggregateTypes(aggregate.sourceLine(), aggregatePackage);
        }
    }

    private void validateAggregateTypes(SourceLine sourceLine, AggregatePackage aggregatePackage) {
        var dataAccessClassName = NamingConventions.aggregateDataAccessTypeName(aggregatePackage);
        if(!model.hasClass(dataAccessClassName)) {
            messages.add(new ValidationMessage.Builder()
                    .location(sourceLine)
                    .type(ValidationMessageType.WARNING)
                    .message("Data access definition missing, misplaced or not following naming convention")
                    .build());
        }

        var attributesImplementationClassName =
                NamingConventions.aggregateAttributesImplementationTypeName(aggregatePackage);
        if(!model.hasClass(attributesImplementationClassName)) {
            messages.add(new ValidationMessage.Builder()
                    .location(sourceLine)
                    .type(ValidationMessageType.WARNING)
                    .message("Attributes implementation missing, misplaced or not following naming convention")
                    .build());
        }

        for(StorageTypesValidator storageTypesValidator : storageTypesValidators) {
            storageTypesValidator.validateDataAccessImplementation(sourceLine, aggregatePackage).ifPresent(messages::add);
            messages.addAll(storageTypesValidator.validateAdditionalTypes(sourceLine, aggregatePackage));
        }
    }

    @Override
    protected String name() {
        return "Types";
    }

    public TypesValidator(ValidationModel model, List<StorageTypesValidator> storageTypesValidators, ClassResolver classResolver) {
        super(model);
        this.storageTypesValidators = new ArrayList<>(storageTypesValidators);

        requireNonNull(classResolver);
        this.classResolver = classResolver;
    }

    private List<StorageTypesValidator> storageTypesValidators;

    public ClassResolver classResolver() {
        return classResolver;
    }

    private ClassResolver classResolver;
}
