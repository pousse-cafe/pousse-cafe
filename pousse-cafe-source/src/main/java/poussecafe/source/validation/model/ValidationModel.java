package poussecafe.source.validation.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
}
