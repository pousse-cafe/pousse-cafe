package poussecafe.source.validation.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import poussecafe.source.validation.model.EntityDefinition;
import poussecafe.source.validation.model.EntityImplementation;

import static java.util.Objects.requireNonNull;

class EntityValidationModel {

    EntityValidationModel(String entityIdentifier) {
        requireNonNull(entityIdentifier);
        this.entityIdentifier = entityIdentifier;
    }

    private String entityIdentifier;

    public String entityIdentifier() {
        return entityIdentifier;
    }

    public void includeDefinition(EntityDefinition definition) {
        requireNonNull(definition);
        definitions.add(definition);
    }

    private List<EntityDefinition> definitions = new ArrayList<>();

    public List<EntityDefinition> definitions() {
        return Collections.unmodifiableList(definitions);
    }

    public void includeImplementation(EntityImplementation implementation) {
        requireNonNull(implementation);
        implementations.add(implementation);
    }

    private List<EntityImplementation> implementations = new ArrayList<>();

    public List<EntityImplementation> implementations() {
        return Collections.unmodifiableList(implementations);
    }

    public boolean hasNoImplementation() {
        return implementations.isEmpty();
    }

    public boolean hasConflictingDefinitions() {
        return definitions.size() > 1;
    }

    public boolean hasConflictingImplementations() {
        var namesCounts = new HashMap<String, Integer>();
        for(EntityImplementation implementation : implementations) {
            var names = implementation.storageNames();
            if(names.isEmpty()) {
                var defaultCount = namesCounts.computeIfAbsent(DEFAULT_MESSAGING_NAME, key -> 0);
                namesCounts.put(DEFAULT_MESSAGING_NAME, defaultCount + 1);
            } else {
                for(String name : names) {
                    var nameCount = namesCounts.computeIfAbsent(name, key -> 0);
                    namesCounts.put(name, nameCount + 1);
                }
            }
        }
        return namesCounts.values().stream()
                .anyMatch(count -> count > 1);
    }

    private static final String DEFAULT_MESSAGING_NAME = "*";
}
