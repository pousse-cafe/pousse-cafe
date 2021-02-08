package poussecafe.source.validation.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import poussecafe.source.analysis.Name;
import poussecafe.source.validation.names.DeclaredComponent;

import static poussecafe.util.Equality.referenceEquals;

@SuppressWarnings("serial")
public class ValidationModel implements Serializable {

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

    private Map<String, Runner> runnersBySourceId = new HashMap<>();

    public Optional<Runner> runner(String runnerClassQualifiedName) {
        return Optional.ofNullable(runners.get(runnerClassQualifiedName));
    }

    public void addModule(Module module) {
        modules.put(module.className(), module);
    }

    private Map<Name, Module> modules = new HashMap<>();

    private Map<String, Module> modulesBySourceId = new HashMap<>();

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

    public void addAggregateRoot(AggregateComponentDefinition aggregateRoot) {
        aggregateRoots.add(aggregateRoot);
    }

    private List<AggregateComponentDefinition> aggregateRoots = new ArrayList<>();

    public List<AggregateComponentDefinition> aggregateRoots() {
        return Collections.unmodifiableList(aggregateRoots);
    }

    public void addAggregateFactory(AggregateComponentDefinition aggregateFactory) {
        aggregateFactories.add(aggregateFactory);
    }

    private List<AggregateComponentDefinition> aggregateFactories = new ArrayList<>();

    public List<AggregateComponentDefinition> aggregateFactories() {
        return Collections.unmodifiableList(aggregateFactories);
    }

    public void addAggregateRepository(AggregateComponentDefinition aggregateRepository) {
        aggregateRepositories.add(aggregateRepository);
    }

    private List<AggregateComponentDefinition> aggregateRepositories = new ArrayList<>();

    public List<AggregateComponentDefinition> aggregateRepositories() {
        return Collections.unmodifiableList(aggregateRepositories);
    }

    public void addDataAccessDefinition(DataAccessDefinition definition) {
        dataAccessDefinitions.add(definition);
    }

    private List<DataAccessDefinition> dataAccessDefinitions = new ArrayList<>();

    public List<DataAccessDefinition> dataAccessDefinitions() {
        return Collections.unmodifiableList(dataAccessDefinitions);
    }

    public void forget(String sourceId) {
        messageDefinitions.removeIf(definition-> definition.sourceLine().isPresent() && definition.sourceLine().orElseThrow().source().id().equals(sourceId));
        messageImplementations.removeIf(definition-> definition.sourceLine().isPresent() && definition.sourceLine().orElseThrow().source().id().equals(sourceId));

        entityDefinitions.removeIf(definition-> definition.sourceLine().isPresent() && definition.sourceLine().orElseThrow().source().id().equals(sourceId));
        entityImplementations.removeIf(definition-> definition.sourceLine().isPresent() && definition.sourceLine().orElseThrow().source().id().equals(sourceId));

        processDefinitions.removeIf(definition-> definition.sourceLine().isPresent() && definition.sourceLine().orElseThrow().source().id().equals(sourceId));

        forget(sourceId, modulesBySourceId,
                component -> modules.remove(component.className()));

        aggregateRoots.removeIf(definition-> definition.sourceLine().isPresent() && definition.sourceLine().orElseThrow().source().id().equals(sourceId));
        aggregateFactories.removeIf(definition-> definition.sourceLine().isPresent() && definition.sourceLine().orElseThrow().source().id().equals(sourceId));
        aggregateRepositories.removeIf(definition-> definition.sourceLine().isPresent() && definition.sourceLine().orElseThrow().source().id().equals(sourceId));

        listeners.removeIf(listener -> listener.sourceLine().source().id().equals(sourceId));

        forget(sourceId, runnersBySourceId,
                component -> runners.remove(component.classQualifiedName()));

        forget(sourceId, dataAccessDefinitions);
    }

    private <T> void forget(
            String sourceId,
            Map<String, T> componentsBySourceId,
            Consumer<T> componentRemover) {
        var component = componentsBySourceId.remove(sourceId);
        if(component != null) {
            componentRemover.accept(component);
        }
    }

    private void forget(String sourceId, List<? extends DeclaredComponent> components) {
        components.removeIf(definition-> definition.sourceLine().isPresent() && definition.sourceLine().orElseThrow().source().id().equals(sourceId));
    }

    @Override
    public boolean equals(Object obj) {
        return referenceEquals(this, obj).orElse(other -> new EqualsBuilder()
                .append(aggregateFactories, other.aggregateFactories)
                .append(aggregateRepositories, other.aggregateRepositories)
                .append(aggregateRoots, other.aggregateRoots)
                .append(entityDefinitions, other.entityDefinitions)
                .append(entityImplementations, other.entityImplementations)
                .append(listeners, other.listeners)
                .append(messageDefinitionClassNames, other.messageDefinitionClassNames)
                .append(messageDefinitions, other.messageDefinitions)
                .append(messageImplementations, other.messageImplementations)
                .append(modules, other.modules)
                .append(modulesBySourceId, other.modulesBySourceId)
                .append(processDefinitions, other.processDefinitions)
                .append(runners, other.runners)
                .append(runnersBySourceId, other.runnersBySourceId)
                .append(dataAccessDefinitions, other.dataAccessDefinitions)
                .build());
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(aggregateFactories)
                .append(aggregateRepositories)
                .append(aggregateRoots)
                .append(entityDefinitions)
                .append(entityImplementations)
                .append(listeners)
                .append(messageDefinitionClassNames)
                .append(messageDefinitions)
                .append(messageImplementations)
                .append(modules)
                .append(modulesBySourceId)
                .append(processDefinitions)
                .append(runners)
                .append(runnersBySourceId)
                .append(dataAccessDefinitions)
                .build();
    }
}
