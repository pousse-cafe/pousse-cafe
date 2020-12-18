package poussecafe.source.validation.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ValidationModel {

    public void addMessageDefinition(MessageDefinition definition) {
        messageDefinitions.add(definition);
    }

    private List<MessageDefinition> messageDefinitions = new ArrayList<>();

    public List<MessageDefinition> messageDefinitions() {
        return Collections.unmodifiableList(messageDefinitions);
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
}
