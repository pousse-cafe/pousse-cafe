package poussecafe.source.pcmil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import poussecafe.source.model.Aggregate;
import poussecafe.source.model.Message;
import poussecafe.source.model.MessageListener;
import poussecafe.source.model.MessageListenerContainerType;
import poussecafe.source.model.MessageType;
import poussecafe.source.model.Model;
import poussecafe.source.model.ProducedEvent;
import poussecafe.source.model.ProductionType;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public class PcMilExporter {

    public String toPcMil() {
        if(pcMil == null) {
            generatePcMil();
        }
        return pcMil;
    }

    private String pcMil;

    private void generatePcMil() {
        builder = new FormattedPcMilTokenStreamBuilder();
        while(!listeners.isEmpty()) {
            MessageListener next = next();
            appendTopListener(next);

            listeners.remove(next);
            if(!listeners.isEmpty()) {
                builder.appendNewLine();
            }
        }
        pcMil = builder.toString();
    }

    private FormattedPcMilTokenStreamBuilder builder;

    private List<MessageListener> listeners;

    private MessageListener next() {
        Optional<MessageListener> topListener = findNextTopListener();
        if(topListener.isEmpty()) {
            return listeners.get(0);
        } else {
            return topListener.get();
        }
    }

    private Optional<MessageListener> findNextTopListener() {
        return listeners.stream()
                .filter(listener -> !producedEvents.contains(listener.consumedMessage()))
                .findFirst();
    }

    private Set<Message> producedEvents = new HashSet<>();

    private void appendTopListener(MessageListener rootListener) {
        builder.resetIndent();
        Optional<String> consumesFromExternal = rootListener.consumesFromExternal();
        if(consumesFromExternal.isPresent()) {
            builder.appendOpeningNote(consumesFromExternal.get());
        }
        appendMessageConsumption(rootListener.consumedMessage(), Optional.empty());
    }

    private void appendMessageConsumption(Message message, Optional<String> noteIfNoListeners) {
        appendMessage(message);

        List<MessageListener> consumers = findAndRemoveConsumers(message);
        if(consumers.isEmpty()) {
            appendNoListener(noteIfNoListeners);
        } else if(consumers.size() == 1) {
            appendSingleListener(consumers.get(0));
        } else {
            appendMultipleListeners(consumers);
        }
    }

    private void appendMessage(Message message) {
        if(message.type() == MessageType.COMMAND) {
            builder.appendCommandIdentifier(message.name());
        } else if(message.type() == MessageType.DOMAIN_EVENT) {
            builder.appendDomainEventIdentifier(message.name());
        }
    }

    private List<MessageListener> findAndRemoveConsumers(Message event) {
        var removedListeners = new ArrayList<MessageListener>();
        Iterator<MessageListener> iterator = listeners.iterator();
        while(iterator.hasNext()) {
            MessageListener listener = iterator.next();
            if(listener.consumedMessage().equals(event)) {
                removedListeners.add(listener);
                iterator.remove();
            }
        }
        return removedListeners;
    }

    private void appendNoListener(Optional<String> noteIfNoListeners) {
        builder.appendEndOfConsumption();
        if(noteIfNoListeners.isPresent()) {
            builder.appendClosingNote(noteIfNoListeners.get());
        }
        builder.appendNewLine();
    }

    private void appendSingleListener(MessageListener consumer) {
        builder.appendConsumptionToken();
        appendListener(consumer);
    }

    private void appendListener(MessageListener listener) {
        if(listener.container().type() == MessageListenerContainerType.FACTORY) {
            appendFactoryListener(listener);
        } else if(listener.container().type() == MessageListenerContainerType.REPOSITORY) {
            appendRepositoryListener(listener);
        } else if(listener.container().type() == MessageListenerContainerType.ROOT) {
            appendAggregateRootListener(listener);
        } else {
            builder.appendEndOfConsumption();
            builder.appendClosingNote("external");
        }
    }

    private void appendMultipleListeners(List<MessageListener> consumers) {
        builder.appendConsumptionToken();
        builder.appendNewLine();
        builder.incrementIndent();
        for(MessageListener listener : consumers) {
            builder.indent();
            builder.appendConsumptionToken();
            appendListener(listener);
        }
    }

    private void appendFactoryListener(MessageListener listener) {
        appendFactoryName(listener);
        appendMessageConsumptions(listener);
    }

    private void appendFactoryName(MessageListener listener) {
        var aggregateName = listener.container().aggregateName().orElseThrow();
        ProductionType productionType = listener.productionType().orElseThrow();
        if(productionType == ProductionType.OPTIONAL) {
            builder.appendOptionalOperator();
        } else if(productionType == ProductionType.SEVERAL) {
            builder.appendSeveralOperator();
        }
        builder.appendFactoryIdentifier(listener.container().aggregateName().orElseThrow());
        builder.appendNewLine();
        var aggregate = model.aggregateRoot(aggregateName).orElseThrow();
        if(!aggregate.onAddProducedEvents().isEmpty()) {
            builder.incrementIndent();
            builder.indent();
            builder.appendAggregateIdentifier(aggregateName);
            builder.appendInlineNote(Aggregate.ON_ADD_METHOD_NAME);
            appendAggregateMessageConsumptions(aggregate.onAddProducedEvents());
        }
    }

    private Model model;

    private void appendMessageConsumptions(MessageListener listener) {
        for(ProducedEvent producedEvent : listener.producedEvents()) {
            Optional<String> note = consumedByExternalNote(producedEvent);
            appendMessageConsumption(producedEvent.message(), note);
        }
    }

    private Optional<String> consumedByExternalNote(ProducedEvent producedEvent) {
        Optional<String> note;
        if(producedEvent.consumedByExternal().isEmpty()) {
            note = Optional.empty();
        } else {
            note = Optional.of(producedEvent.consumedByExternal().stream().collect(joining(", ")));
        }
        return note;
    }

    private void appendRepositoryListener(MessageListener listener) {
        appendRepositoryName(listener);
        appendMessageConsumptions(listener);
    }

    private void appendRepositoryName(MessageListener listener) {
        String aggregateName = listener.container().aggregateName().orElseThrow();
        builder.appendRepositoryIdentifier(aggregateName);
        builder.appendNewLine();
        var aggregate = model.aggregateRoot(aggregateName).orElseThrow();
        if(!aggregate.onDeleteProducedEvents().isEmpty()) {
            builder.incrementIndent();
            builder.indent();
            builder.appendAggregateIdentifier(aggregateName);
            builder.appendInlineNote(Aggregate.ON_DELETE_METHOD_NAME);
            appendAggregateMessageConsumptions(aggregate.onDeleteProducedEvents());
        }
    }

    private void appendAggregateRootListener(MessageListener listener) {
        appendRunnerAndAggregateRoot(listener);
        var listenerProducedEvents = aggregateRootListenerProducedEvents(listener);
        if(!listenerProducedEvents.isEmpty()) {
            appendAggregateMessageConsumptions(listenerProducedEvents);
        } else {
            builder.appendNewLine();
        }
    }

    private List<ProducedEvent> aggregateRootListenerProducedEvents(MessageListener listener) {
        var eventsSet = new HashSet<ProducedEvent>();
        eventsSet.addAll(listener.producedEvents());

        String aggregateName = listener.container().aggregateName().orElseThrow();
        var aggregate = model.aggregateRoot(aggregateName).orElseThrow();
        eventsSet.addAll(aggregate.onUpdateProducedEvents());

        var listenerProducedEvents = new ArrayList<>(eventsSet);
        listenerProducedEvents.sort((e1, e2) -> e1.message().name().compareTo(e2.message().name()));

        return listenerProducedEvents;
    }

    private void appendRunnerAndAggregateRoot(MessageListener listener) {
        builder.appendRunnerIdentifier(listener.runnerName().orElse(""));
        builder.appendNewLine();
        builder.incrementIndent();
        builder.indent();
        builder.appendAggregateIdentifier(listener.container().aggregateName().orElseThrow());
        builder.appendInlineNote(listener.methodName());
    }

    private void appendAggregateMessageConsumptions(List<ProducedEvent> events) {
        builder.appendOpenRelation();
        builder.appendNewLine();
        builder.incrementIndent();
        for(ProducedEvent producedEvent : events) {
            builder.indent();
            builder.appendCloseRelation();
            if(!producedEvent.required()) {
                builder.appendOptionalOperator();
            }
            Optional<String> note = consumedByExternalNote(producedEvent);
            appendMessageConsumption(producedEvent.message(), note);
        }
    }

    public static class Builder {

        private PcMilExporter exporter = new PcMilExporter();

        public PcMilExporter build() {
            requireNonNull(exporter.model);
            requireNonNull(processName);

            exporter.listeners = new LinkedList<>(exporter.model.processListeners(processName));
            exporter.listeners.sort(new PcMilListenersComparator());

            exporter.producedEvents = new HashSet<>();
            exporter.listeners.stream().forEach(listener -> exporter.producedEvents.addAll(
                    listener.producedEvents().stream().map(ProducedEvent::message).collect(toList())));

            return exporter;
        }

        public Builder model(Model model) {
            exporter.model = model;
            return this;
        }

        public Builder processName(String processName) {
            this.processName = processName;
            return this;
        }

        private String processName;
    }

    private PcMilExporter() {

    }
}
