package poussecafe.source.pcmil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
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
        while(!processListeners.isEmpty()) {
            MessageListener next = next();
            appendTopListener(next);

            processListeners.remove(next);
            if(!processListeners.isEmpty()) {
                builder.appendNewLine();
            }
        }
        pcMil = builder.toString();
    }

    private FormattedPcMilTokenStreamBuilder builder;

    private List<MessageListener> processListeners;

    private MessageListener next() {
        Optional<MessageListener> topListener = findNextTopListener();
        if(topListener.isEmpty()) {
            return processListeners.get(0);
        } else {
            return topListener.get();
        }
    }

    private Optional<MessageListener> findNextTopListener() {
        return processListeners.stream()
                .filter(listener -> !processProducedEvents.contains(listener.consumedMessage()))
                .findFirst();
    }

    private Set<Message> processProducedEvents = new HashSet<>();

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

        var consumers = findAndRemoveConsumers(message);
        if(consumers.isEmpty()) {
            appendNoListener(noteIfNoListeners);
        } else if(consumers.size() == 1) {
            appendSingleListener(consumers);
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

    private Consumers findAndRemoveConsumers(Message event) {
        var consumers = new Consumers();

        Iterator<MessageListener> iterator = processListeners.iterator();
        while(iterator.hasNext()) {
            MessageListener listener = iterator.next();
            if(listener.consumedMessage().equals(event)) {
                consumers.listeners.add(listener);
                iterator.remove();
            }
        }

        var uniqueOrderedProcessNames = new TreeSet<String>();
        model.messageListeners().stream()
                .filter(listener -> !listener.processNames().contains(processName))
                .filter(listener -> listener.consumedMessage().equals(event))
                .forEach(listener -> uniqueOrderedProcessNames.addAll(listener.processNames()));
        consumers.processes.addAll(uniqueOrderedProcessNames);

        return consumers;
    }

    class Consumers {

        List<MessageListener> listeners = new ArrayList<>();

        List<String> processes = new ArrayList<>();

        public boolean isEmpty() {
            return listeners.isEmpty()
                    && processes.isEmpty();
        }

        public int size() {
            return listeners.size() + processes.size();
        }
    }

    private String processName;

    private void appendNoListener(Optional<String> noteIfNoListeners) {
        builder.appendEndOfConsumption();
        if(noteIfNoListeners.isPresent()) {
            builder.appendClosingNote(noteIfNoListeners.get());
        }
        builder.appendNewLine();
    }

    private void appendSingleListener(Consumers consumers) {
        builder.appendConsumptionToken();
        if(consumers.listeners.size() == 1) {
            appendListener(consumers.listeners.get(0));
        } else {
            appendProcess(consumers.processes.get(0));
        }
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

    private void appendMultipleListeners(Consumers consumers) {
        builder.appendOpeningConsumptionToken();
        builder.appendNewLine();
        builder.incrementIndent();
        for(MessageListener listener : consumers.listeners) {
            builder.indent();
            builder.appendClosingConsumptionToken();
            appendListener(listener);
        }
        for(String process : consumers.processes) {
            builder.indent();
            builder.appendClosingConsumptionToken();
            appendProcess(process);
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
            builder.decrementIndent();
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
            builder.decrementIndent();
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
        builder.decrementIndent();
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
        builder.decrementIndent();
    }

    private void appendProcess(String processName) {
        builder.appendProcessIdentifier(processName);
        builder.appendNewLine();
    }

    public static class Builder {

        private PcMilExporter exporter = new PcMilExporter();

        public PcMilExporter build() {
            requireNonNull(exporter.model);
            requireNonNull(exporter.processName);

            exporter.processListeners = new LinkedList<>(exporter.model.processListeners(exporter.processName));
            exporter.processListeners.sort(new PcMilListenersComparator());

            exporter.processProducedEvents = new HashSet<>();
            exporter.processListeners.stream().forEach(listener -> exporter.processProducedEvents.addAll(
                    listener.producedEvents().stream().map(ProducedEvent::message).collect(toList())));

            return exporter;
        }

        public Builder model(Model model) {
            exporter.model = model;
            return this;
        }

        public Builder processName(String processName) {
            exporter.processName = processName;
            return this;
        }
    }

    private PcMilExporter() {

    }
}
