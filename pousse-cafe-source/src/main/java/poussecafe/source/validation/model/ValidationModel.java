package poussecafe.source.validation.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import poussecafe.source.analysis.Name;

public class ValidationModel {

    public void addMessageDefinition(MessageDefinition definition) {
        messageDefinitions.add(definition);
        messageDefinitionClassNames.add(definition.qualifiedClassName());
    }

    private List<MessageDefinition> messageDefinitions = new ArrayList<>();

    public List<MessageDefinition> messageDefinitions() {
        return Collections.unmodifiableList(messageDefinitions);
    }

    private Set<String> messageDefinitionClassNames = new HashSet<>();

    public boolean hasMessageDefinition(String qualifiedClassName) {
        return messageDefinitionClassNames.contains(qualifiedClassName);
    }

    public void addMessageImplementation(MessageImplementation implementation) {
        messageImplementations.add(implementation);
    }

    private List<MessageImplementation> messageImplementations = new ArrayList<>();

    public List<MessageImplementation> messageImplementations() {
        return Collections.unmodifiableList(messageImplementations);
    }

    public void addEntityDefinition(EntityDefinition definition) {
        entityDefinitions.add(definition);
    }

    private List<EntityDefinition> entityDefinitions = new ArrayList<>();

    public List<EntityDefinition> entityDefinitions() {
        return Collections.unmodifiableList(entityDefinitions);
    }

    public void addEntityImplementation(EntityImplementation implementation) {
        entityImplementations.add(implementation);
    }

    private List<EntityImplementation> entityImplementations = new ArrayList<>();

    public List<EntityImplementation> entityImplementations() {
        return entityImplementations;
    }

    public void addMessageListener(MessageListener listener) {
        listeners.add(listener);
    }

    private List<MessageListener> listeners = new ArrayList<>();

    public List<MessageListener> messageListeners() {
        return Collections.unmodifiableList(listeners);
    }

    public void addRunner(Runner runner) {
        runners.put(runner.classQualifiedName(), runner);
    }

    private Map<String, Runner> runners = new HashMap<>();

    public Optional<Runner> runner(String runnerClassQualifiedName) {
        return Optional.ofNullable(runners.get(runnerClassQualifiedName));
    }

    public void addModule(Module module) {
        modules.put(module.className(), module);
    }

    private Map<Name, Module> modules = new HashMap<>();

    public Collection<Module> modules() {
        return Collections.unmodifiableCollection(modules.values());
    }

    public void addProcessDefinition(ProcessDefinition processDefinition) {
        processDefinitions.add(processDefinition);
    }

    private List<ProcessDefinition> processDefinitions = new ArrayList<>();

    public List<ProcessDefinition> processes() {
        return Collections.unmodifiableList(processDefinitions);
    }

    public void addClassPathModule(Module module) {
        if(!modules.containsKey(module.className())) {
            addModule(module);
        }
    }
}
