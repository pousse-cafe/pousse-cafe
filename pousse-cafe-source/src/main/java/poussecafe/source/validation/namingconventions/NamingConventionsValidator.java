package poussecafe.source.validation.namingconventions;

import java.util.List;
import poussecafe.source.validation.SubValidator;
import poussecafe.source.validation.ValidationMessage;
import poussecafe.source.validation.ValidationMessageType;
import poussecafe.source.validation.model.HasClassNameConvention;
import poussecafe.source.validation.model.StorageImplementationKind;
import poussecafe.source.validation.model.ValidationModel;

import static java.util.stream.Collectors.toList;

public class NamingConventionsValidator extends SubValidator {

    @Override
    public void validate() {
        warnWrongComponentName(
                model.aggregateRoots(),
                "Aggregate root");
        warnWrongComponentName(
                model.aggregateFactories(),
                "Aggregate factory");
        warnWrongComponentName(
                model.aggregateRepositories(),
                "Aggregate repository");
        warnWrongComponentName(
                model.entityImplementations().stream()
                    .filter(implementation -> implementation.kind() == StorageImplementationKind.ATTRIBUTES)
                    .collect(toList()),
                "Entity implementation");
        warnWrongComponentName(
                model.dataAccessDefinitions(),
                "Data access definition");
        warnWrongComponentName(
                model.entityImplementations().stream()
                    .filter(implementation -> implementation.kind() == StorageImplementationKind.DATA_ACCESS)
                    .collect(toList()),
                "Data access implementation");
        warnWrongComponentName(
                model.messageImplementations().stream()
                    .filter(implementation -> !implementation.isAutoImplementation())
                    .collect(toList()),
                "Message implementation");
    }

    private void warnWrongComponentName(
            List<? extends HasClassNameConvention> definitions,
            String componentName) {
        for(HasClassNameConvention definition : definitions) {
            if(!definition.validClassName()) {
                messages.add(new ValidationMessage.Builder()
                        .location(definition.sourceLine().orElseThrow())
                        .type(ValidationMessageType.WARNING)
                        .message(componentName + " name does not follow naming convention")
                        .build());
            }
        }
    }

    @Override
    protected String name() {
        return "Naming conventions";
    }

    public NamingConventionsValidator(ValidationModel model) {
        super(model);
    }
}
