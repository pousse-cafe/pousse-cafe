package poussecafe.source.validation.types;

import java.util.List;
import java.util.Optional;
import poussecafe.source.analysis.ClassResolver;
import poussecafe.source.generation.AggregatePackage;
import poussecafe.source.generation.NamingConventions;
import poussecafe.source.validation.SourceLine;
import poussecafe.source.validation.ValidationMessage;
import poussecafe.source.validation.ValidationMessageType;
import poussecafe.source.validation.model.ValidationModel;

import static java.util.Collections.emptyList;
import static java.util.Objects.requireNonNull;

public abstract class StorageTypesValidator {

    public Optional<ValidationMessage> validateDataAccessImplementation(SourceLine sourceLine, AggregatePackage aggregatePackage) {
        var dataAccessImplementationClassName =
                NamingConventions.aggregateDataAccessImplementationTypeName(aggregatePackage, storageName());
        if(!model.hasClass(dataAccessImplementationClassName)) {
            return Optional.of(new ValidationMessage.Builder()
                    .location(sourceLine)
                    .type(ValidationMessageType.WARNING)
                    .message("Data access implementation for storage " + storageName()
                            + " missing, misplaced or not following naming convention")
                    .build());
        } else {
            return Optional.empty();
        }
    }

    public List<ValidationMessage> validateAdditionalTypes(SourceLine sourceLine, AggregatePackage aggregatePackage) {
        return emptyList();
    }

    protected abstract String storageName();

    public void setModel(ValidationModel model) {
        requireNonNull(model);
        this.model = model;
    }

    private ValidationModel model;

    public ValidationModel model() {
        return model;
    }

    public void setClassResolver(ClassResolver classResolver) {
        requireNonNull(classResolver);
        this.classResolver = classResolver;
    }

    private ClassResolver classResolver;

    public ClassResolver classResolver() {
        return classResolver;
    }
}
