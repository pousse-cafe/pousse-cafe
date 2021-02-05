package poussecafe.source.validation.namingconventions;

import java.util.List;
import java.util.function.Predicate;
import poussecafe.source.generation.NamingConventions;
import poussecafe.source.validation.SubValidator;
import poussecafe.source.validation.ValidationMessage;
import poussecafe.source.validation.ValidationMessageType;
import poussecafe.source.validation.model.AggregateComponentDefinition;
import poussecafe.source.validation.model.ValidationModel;

public class NamingConventionsValidator extends SubValidator {

    @Override
    public void validate() {
        warnWrongComponentName(
                model.aggregateRootsDefinitions(),
                NamingConventions::isAggregateRootName,
                NamingConventions::isInnerAggregateRootName,
                "Aggregate root");
        warnWrongComponentName(
                model.aggregateFactories(),
                NamingConventions::isAggregateFactoryName,
                NamingConventions::isInnerAggregateFactoryName,
                "Aggregate factory");
        warnWrongComponentName(
                model.aggregateRepositories(),
                NamingConventions::isAggregateRepositoryName,
                NamingConventions::isInnerAggregateRepositoryName,
                "Aggregate repository");
    }

    private void warnWrongComponentName(
            List<AggregateComponentDefinition> definitions,
            Predicate<String> standaloneNameValidator,
            Predicate<String> innerNameValidator,
            String componentName) {
        for(AggregateComponentDefinition definition : definitions) {
            if(!definition.isInnerClass()
                    && !standaloneNameValidator.test(definition.className().simple())) {
                messages.add(new ValidationMessage.Builder()
                        .location(definition.sourceLine().orElseThrow())
                        .type(ValidationMessageType.WARNING)
                        .message(componentName + " name does not follow naming convention")
                        .build());
            }

            if(definition.isInnerClass()
                    && !innerNameValidator.test(definition.className().simple())) {
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
