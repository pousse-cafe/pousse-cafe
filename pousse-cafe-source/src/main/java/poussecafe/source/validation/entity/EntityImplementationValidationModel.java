package poussecafe.source.validation.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import poussecafe.source.validation.model.EntityImplementation;

import static java.util.Objects.requireNonNull;

class EntityImplementationValidationModel {

    static String implementationId(EntityImplementation implementation) {
        return implementation.entityImplementationQualifiedClassName().orElseThrow();
    }

    EntityImplementationValidationModel(EntityImplementation implementation) {
        requireNonNull(implementation);
        this.implementation = implementation;
    }

    private EntityImplementation implementation;

    public EntityImplementation implementation() {
        return implementation;
    }

    public String implementationIdentifier() {
        return implementationId(implementation);
    }

    public void includeDefinition(String definition) {
        requireNonNull(definition);
        definitions.add(definition);
    }

    private List<String> definitions = new ArrayList<>();

    public List<String> definitions() {
        return Collections.unmodifiableList(definitions);
    }

    public boolean hasNoDefinition() {
        return definitions.isEmpty();
    }

    public boolean hasConflictingDefinitions() {
        return definitions.size() > 1;
    }
}
