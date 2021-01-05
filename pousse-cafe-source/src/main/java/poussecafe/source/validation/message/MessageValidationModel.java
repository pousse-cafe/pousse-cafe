package poussecafe.source.validation.message;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import poussecafe.source.validation.model.MessageDefinition;
import poussecafe.source.validation.model.MessageImplementation;

import static java.util.Objects.requireNonNull;

class MessageValidationModel {

    MessageValidationModel(String messageIdentifier) {
        requireNonNull(messageIdentifier);
        this.messageIdentifier = messageIdentifier;
    }

    private String messageIdentifier;

    public String messageIdentifier() {
        return messageIdentifier;
    }

    public void includeDefinition(MessageDefinition definition) {
        requireNonNull(definition);
        definitions.add(definition);
    }

    private List<MessageDefinition> definitions = new ArrayList<>();

    public List<MessageDefinition> definitions() {
        return Collections.unmodifiableList(definitions);
    }

    public void includeImplementation(MessageImplementation implementation) {
        requireNonNull(implementation);
        implementations.add(implementation);
    }

    private List<MessageImplementation> implementations = new ArrayList<>();

    public List<MessageImplementation> implementations() {
        return Collections.unmodifiableList(implementations);
    }

    public boolean hasNoImplementation() {
        return implementations.isEmpty();
    }

    public boolean hasConflictingDefinitions() {
        return definitions.size() > 1;
    }

    public boolean hasNoDefinition() {
        return definitions.isEmpty();
    }

    public boolean hasConflictingImplementations() {
        var namesCounts = new HashMap<String, Integer>();
        for(MessageImplementation implementation : implementations) {
            var names = implementation.messagingNames();
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
