package poussecafe.source.validation.names;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.Function;
import poussecafe.source.analysis.Name;
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

    @Override
    protected String name() {
        return "Names";
    }

    private void checkModulesUniqueness() {
        checkNameUniqueness(model.modules(), module -> new Name(module.name()), "Module", false);
    }

    private <T extends NamedComponent> void checkNameUniqueness(
            Collection<T> items,
            Function<T, Name> nameProvider,
            String componentName,
            boolean warnIfNotQualified) {
        var names = new HashMap<Name, List<T>>();
        for(T component : items) {
            var componentsWithName = names.computeIfAbsent(nameProvider.apply(component), key -> new ArrayList<>());
            componentsWithName.add(component);
        }
        for(Entry<Name, List<T>> entry : names.entrySet()) {
            var name = entry.getKey();
            var componentsWithSameName = entry.getValue();
            if(componentsWithSameName.size() > 1) {
                for(T component : componentsWithSameName) {
                    var sourceFileLine = component.sourceLine();
                    if(sourceFileLine.isPresent()) {
                        messages.add(new ValidationMessage.Builder()
                                .location(sourceFileLine.get())
                                .type(ValidationMessageType.ERROR)
                                .message(componentName + " with same name already exists")
                                .build());
                    }
                }
            } else {
                var component = componentsWithSameName.get(0);
                var sourceFileLine = component.sourceLine();
                if(warnIfNotQualified && !name.isQualifiedName() && sourceFileLine.isPresent()) {
                    messages.add(new ValidationMessage.Builder()
                            .location(sourceFileLine.get())
                            .type(ValidationMessageType.WARNING)
                            .message(componentName + " is in default module")
                            .build());
                }
            }
        }
    }

    private void checkEntitiesUniqueness(Modules modules) {
        checkNameUniqueness(model.entityDefinitions(), modules::qualifyName, "Entity", true);
    }

    private void checkMessagesUniqueness(Modules modules) {
        checkNameUniqueness(model.messageDefinitions(), modules::qualifyName, "Message", true);
    }

    private void checkProcessesUniqueness(Modules modules) {
        checkNameUniqueness(model.processes(), modules::qualifyName, "Process", true);
    }

    public NamesValidator(ValidationModel model) {
        super(model);
    }
}
