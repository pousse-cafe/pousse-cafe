package poussecafe.source.validation.names;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;
import poussecafe.source.validation.SubValidator;
import poussecafe.source.validation.ValidationMessage;
import poussecafe.source.validation.ValidationMessageType;
import poussecafe.source.validation.model.ValidationModel;

public class NamesValidator extends SubValidator {

    @Override
    public void validate() {
        checkModulesUniqueness();

        Modules modules = new Modules(model.modules());
        messages.addAll(modules.checkModulesPartition());

        checkEntitiesUniqueness(modules);
        checkMessagesUniqueness(modules);
        checkProcessesUniqueness(modules);
    }

    private void checkModulesUniqueness() {
        checkNameUniqueness(model.modules(), NamedComponent::name, "Module");
    }

    private <T extends NamedComponent> void checkNameUniqueness(
            Collection<T> items,
            Function<T, String> nameProvider,
            String componentName) {
        var names = new HashMap<String, List<T>>();
        for(T component : items) {
            var componentsWithName = names.computeIfAbsent(nameProvider.apply(component), key -> new ArrayList<>());
            componentsWithName.add(component);
        }
        for(List<T> componentsWithSameName : names.values()) {
            if(componentsWithSameName.size() > 1) {
                for(T component : componentsWithSameName) {
                    messages.add(new ValidationMessage.Builder()
                            .location(component.sourceFileLine())
                            .type(ValidationMessageType.ERROR)
                            .message(componentName + " with same name already exists")
                            .build());
                }
            }
        }
    }

    private void checkEntitiesUniqueness(Modules modules) {
        checkNameUniqueness(model.entityDefinitions(), modules::qualifyName, "Entity");
    }

    private void checkMessagesUniqueness(Modules modules) {
        checkNameUniqueness(model.messageDefinitions(), modules::qualifyName, "Message");
    }

    private void checkProcessesUniqueness(Modules modules) {
        checkNameUniqueness(model.processes(), modules::qualifyName, "Process");
    }

    public NamesValidator(ValidationModel model) {
        super(model);
    }
}
