package poussecafe.source.validation.names;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import poussecafe.source.analysis.Name;
import poussecafe.source.validation.ValidationMessage;
import poussecafe.source.validation.ValidationMessageType;
import poussecafe.source.validation.model.Module;

public class Modules {

    public Modules(Collection<Module> modules) {
        sortedModules = new Module[modules.size()];
        modules.toArray(sortedModules);
        Arrays.sort(sortedModules, moduleComparator);
    }

    private Module[] sortedModules;

    private Comparator<Module> moduleComparator = (m1, m2) -> m1.basePackage().compareTo(m2.basePackage());

    public List<ValidationMessage> checkModulesPartition() {
        var messages = new ArrayList<ValidationMessage>();
        for(int i = sortedModules.length - 1; i >= 1; --i) {
            var sourceFileLine = sortedModules[i].sourceLine();
            if(sourceFileLine.isPresent()
                    && sortedModules[i].basePackage().startsWith(sortedModules[i - 1].basePackage())) {
                messages.add(new ValidationMessage.Builder()
                        .location(sourceFileLine.get())
                        .type(ValidationMessageType.ERROR)
                        .message("Base package is a subpackage of module " + sortedModules[i - 1].className())
                        .build());
            }
        }
        return messages;
    }

    public Name qualifyName(NamedComponent component) {
        var componentQualifiedClassName = component.className().qualified();
        for(int i = sortedModules.length - 1; i >= 0; --i) {
            if(componentQualifiedClassName.startsWith(sortedModules[i].basePackage() + ".")) {
                return new Name(sortedModules[i].name(), component.name());
            }
        }
        return new Name(component.name());
    }
}
