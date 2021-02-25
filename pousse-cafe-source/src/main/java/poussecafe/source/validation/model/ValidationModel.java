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
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import poussecafe.source.analysis.ClassName;
import poussecafe.source.validation.SourceLine;
import poussecafe.source.validation.names.DeclaredComponent;

import static poussecafe.util.Equality.referenceEquals;

@SuppressWarnings("serial")
public class ValidationModel implements Serializable {

    public void addMessageDefinition(MessageDefinition definition) {
        messageDefinitions.add(definition);
        messageDefinitionClassNames.add(definition.qualifiedClassName());
        addClass(definition);
    }

    private void addClass(DeclaredComponent component) {
        classes.add(component.className());
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
        addClass(implementation);
    }

    private List<MessageImplementation> messageImplementations = new ArrayList<>();

    public List<MessageImplementation> messageImplementations() {
        return Collections.unmodifiableList(messageImplementations);
    }

    public void addEntityDefinition(EntityDefinition definition) {
        entityDefinitions.add(definition);
        addClass(definition);
    }

    private List<EntityDefinition> entityDefinitions = new ArrayList<>();

    public List<EntityDefinition> entityDefinitions() {
        return Collections.unmodifiableList(entityDefinitions);
    }

    public void addEntityImplementation(EntityImplementation implementation) {
        entityImplementations.add(implementation);
        addClass(implementation);
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
        runners.put(runner.className().qualified(), runner);
        addClass(runner);
    }

    private Map<String, Runner> runners = new HashMap<>();

    public Optional<Runner> runner(String runnerClassQualifiedName) {
        return Optional.ofNullable(runners.get(runnerClassQualifiedName));
    }

    public void addModule(Module module) {
        modules.put(module.className(), module);
        addClass(module);
    }

    private Map<ClassName, Module> modules = new HashMap<>();

    public Collection<Module> modules() {
        return Collections.unmodifiableCollection(modules.values());
    }

    public void addProcessDefinition(ProcessDefinition processDefinition) {
        processDefinitions.add(processDefinition);
        addClass(processDefinition);
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
        addClass(aggregateRoot);
    }

    private List<AggregateComponentDefinition> aggregateRoots = new ArrayList<>();

    public List<AggregateComponentDefinition> aggregateRoots() {
        return Collections.unmodifiableList(aggregateRoots);
    }

    public void addAggregateFactory(AggregateComponentDefinition aggregateFactory) {
        aggregateFactories.add(aggregateFactory);
        addClass(aggregateFactory);
    }

    private List<AggregateComponentDefinition> aggregateFactories = new ArrayList<>();

    public List<AggregateComponentDefinition> aggregateFactories() {
        return Collections.unmodifiableList(aggregateFactories);
    }

    public void addAggregateRepository(AggregateComponentDefinition aggregateRepository) {
        aggregateRepositories.add(aggregateRepository);
        addClass(aggregateRepository);
    }

    private List<AggregateComponentDefinition> aggregateRepositories = new ArrayList<>();

    public List<AggregateComponentDefinition> aggregateRepositories() {
        return Collections.unmodifiableList(aggregateRepositories);
    }

    public void addDataAccessDefinition(DataAccessDefinition definition) {
        dataAccessDefinitions.add(definition);
        addClass(definition);
    }

    private List<DataAccessDefinition> dataAccessDefinitions = new ArrayList<>();

    public List<DataAccessDefinition> dataAccessDefinitions() {
        return Collections.unmodifiableList(dataAccessDefinitions);
    }

    public void forget(String sourceId) {
        forget(sourceId, messageDefinitions);
        forget(sourceId, messageImplementations);

        forget(sourceId, entityDefinitions);
        forget(sourceId, entityImplementations);

        forget(sourceId, processDefinitions);

        forget(sourceId, modules.values());

        forget(sourceId, aggregateRoots);
        forget(sourceId, aggregateFactories);
        forget(sourceId, aggregateRepositories);

        listeners.removeIf(listener -> listener.sourceLine().source().id().equals(sourceId));

        forget(sourceId, runners.values());
        forget(sourceId, dataAccessDefinitions);
    }

    private void forget(
            String sourceId,
            Collection<? extends DeclaredComponent> components) {
        var iterator = components.iterator();
        while(iterator.hasNext()) {
            var next = iterator.next();
            if(next.sourceLine().isPresent()
                    && next.sourceLine().orElseThrow().source().id().equals(sourceId)) {
                classes.remove(next.className());
                iterator.remove();
            }
        }
    }

    public boolean hasClass(ClassName className) {
        return classes.contains(className);
    }

    private Set<ClassName> classes = new HashSet<>();

    public List<AggregateContainer> aggregateContainers() {
        return Collections.unmodifiableList(aggregateContainers);
    }

    private List<AggregateContainer> aggregateContainers = new ArrayList<>();

    public void addAggregateContainer(AggregateContainer aggregateContainer) {
        aggregateContainers.add(aggregateContainer);
    }

    public void addIgnoredProducesEventAnnotation(SourceLine line) {
        ignoredProducesEventAnnotations.add(line);
    }

    private List<SourceLine> ignoredProducesEventAnnotations = new ArrayList<>();

    public List<SourceLine> ignoredProducesEventAnnotations() {
        return Collections.unmodifiableList(ignoredProducesEventAnnotations);
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
                .append(processDefinitions, other.processDefinitions)
                .append(runners, other.runners)
                .append(dataAccessDefinitions, other.dataAccessDefinitions)
                .append(aggregateContainers, other.aggregateContainers)
                .append(ignoredProducesEventAnnotations, other.ignoredProducesEventAnnotations)
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
                .append(processDefinitions)
                .append(runners)
                .append(dataAccessDefinitions)
                .append(aggregateContainers)
                .append(ignoredProducesEventAnnotations)
                .build();
    }
}
