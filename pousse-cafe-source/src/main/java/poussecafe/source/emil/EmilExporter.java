package poussecafe.source.emil;

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

public class EmilExporter {

    public String toPcMil() {
        if(pcMil == null) {
            generatePcMil();
        }
        return pcMil;
    }

    private String pcMil;

    private void generatePcMil() {
        builder = new FormattedEmilTokenStreamBuilder();
        while(!targetListeners.isEmpty()) {
            MessageListener next = next();
            appendTopListener(next);

            targetListeners.remove(next);
            if(!targetListeners.isEmpty()) {
                builder.appendNewLine();
            }
        }
        pcMil = builder.toString();
    }

    private FormattedEmilTokenStreamBuilder builder;

    private List<MessageListener> targetListeners;

    private MessageListener next() {
        Optional<MessageListener> topListener = findNextTopListener();
        if(topListener.isEmpty()) {
            return targetListeners.get(0);
        } else {
            return topListener.get();
        }
    }

    private Optional<MessageListener> findNextTopListener() {
        return targetListeners.stream()
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
        appendMessageConsumption(rootListener.consumedMessage(), true, Optional.empty());
    }

    private void appendMessageConsumption(Message message, boolean required, Optional<String> noteIfNoListeners) {
        appendMessage(message, required);

        var consumers = findAndRemoveConsumers(message);
        if(consumers.isEmpty()) {
            appendNoListener(noteIfNoListeners);
        } else if(consumers.size() == 1) {
            appendSingleListener(consumers);
        } else {
            appendMultipleListeners(consumers);
        }
    }

    private void appendMessage(Message message, boolean required) {
        if(message.type() == MessageType.COMMAND) {
            builder.appendCommandIdentifier(message.name());
        } else if(message.type() == MessageType.DOMAIN_EVENT) {
            builder.appendDomainEventIdentifier(message.name());
        }
        if(!required) {
            builder.appendOptionalOperator();
        }
    }

    private Consumers findAndRemoveConsumers(Message event) {
        var consumers = new Consumers();

        Iterator<MessageListener> iterator = targetListeners.iterator();
        while(iterator.hasNext()) {
            MessageListener listener = iterator.next();
            if(listener.consumedMessage().equals(event)) {
                consumers.listeners.add(listener);
                iterator.remove();
            }
        }

        if(processName.isPresent()) {
            var uniqueOrderedProcessNames = new TreeSet<String>();
            model.messageListeners().stream()
                    .filter(listener -> !listener.processNames().contains(processName.get()))
                    .filter(listener -> listener.consumedMessage().equals(event))
                    .forEach(listener -> uniqueOrderedProcessNames.addAll(listener.processNames()));
            consumers.otherProcesses.addAll(uniqueOrderedProcessNames);
        }

        return consumers;
    }

    class Consumers {

        List<MessageListener> listeners = new ArrayList<>();

        List<String> otherProcesses = new ArrayList<>();

        public boolean isEmpty() {
            return listeners.isEmpty()
                    && otherProcesses.isEmpty();
        }

        public int size() {
            return listeners.size() + otherProcesses.size();
        }
    }

    private Optional<String> processName;

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
            appendProcess(consumers.otherProcesses.get(0));
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
        for(String process : consumers.otherProcesses) {
            builder.indent();
            builder.appendClosingConsumptionToken();
            appendProcess(process);
        }
    }

    private void appendFactoryListener(MessageListener listener) {
        appendFactoryName(listener);

        var aggregateName = listener.container().aggregateName().orElseThrow();
        var aggregate = model.aggregateRoot(aggregateName).orElseThrow();

        var allProducedEvents = new ArrayList<ProducedEvent>();
        allProducedEvents.addAll(aggregate.onAddProducedEvents());
        allProducedEvents.addAll(listener.producedEvents());

        appendMessageConsumptions(listener, Aggregate.ON_ADD_METHOD_NAME, allProducedEvents);
    }

    private void appendFactoryName(MessageListener listener) {
        builder.appendFactoryIdentifier(listener.container().className());
        builder.appendInlineNote(listener.methodName());
        ProductionType productionType = listener.productionType().orElseThrow();
        if(productionType == ProductionType.OPTIONAL) {
            builder.appendOptionalOperator();
        } else if(productionType == ProductionType.SEVERAL) {
            builder.appendSeveralOperator();
        }
        builder.appendNewLine();
    }

    private Model model;

    private void appendMessageConsumptions(MessageListener listener, String hookName, List<ProducedEvent> hookProducedEvents) {
        if(!hookProducedEvents.isEmpty()) {
            builder.incrementIndent();
            builder.indent();

            var aggregateName = listener.container().aggregateName().orElseThrow();
            builder.appendAggregateIdentifier(aggregateName);
            builder.appendInlineNote(hookName);
            appendAggregateMessageConsumptions(hookProducedEvents);

            builder.decrementIndent();
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

        var aggregateName = listener.container().aggregateName().orElseThrow();
        var aggregate = model.aggregateRoot(aggregateName).orElseThrow();

        var allProducedEvents = new ArrayList<ProducedEvent>();
        allProducedEvents.addAll(aggregate.onDeleteProducedEvents());
        allProducedEvents.addAll(listener.producedEvents());

        appendMessageConsumptions(listener, Aggregate.ON_DELETE_METHOD_NAME, allProducedEvents);
    }

    private void appendRepositoryName(MessageListener listener) {
        builder.appendRepositoryIdentifier(listener.container().className());
        builder.appendInlineNote(listener.methodName());
        builder.appendNewLine();
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
        builder.appendRunnerIdentifier(listener.runnerName().orElseThrow());
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
            Optional<String> note = consumedByExternalNote(producedEvent);
            appendMessageConsumption(producedEvent.message(), producedEvent.required(), note);
        }
        builder.decrementIndent();
    }

    private void appendProcess(String processName) {
        builder.appendProcessIdentifier(processName);
        builder.appendNewLine();
    }

    public static class Builder {

        private EmilExporter exporter = new EmilExporter();

        public EmilExporter build() {
            requireNonNull(exporter.model);
            requireNonNull(exporter.processName);

            if(exporter.processName.isPresent()) {
                exporter.targetListeners = new LinkedList<>(exporter.model.processListeners(
                        exporter.processName.orElseThrow()));
            } else {
                exporter.targetListeners = new LinkedList<>(exporter.model.messageListeners());
            }
            exporter.targetListeners.sort(new EmilListenersComparator());

            exporter.processProducedEvents = new HashSet<>();
            exporter.targetListeners.stream().forEach(listener -> exporter.processProducedEvents.addAll(
                    listener.producedEvents().stream().map(ProducedEvent::message).collect(toList())));

            return exporter;
        }

        public Builder model(Model model) {
            exporter.model = model;
            return this;
        }

        public Builder processName(Optional<String> processName) {
            exporter.processName = processName;
            return this;
        }
    }

    private EmilExporter() {

    }
}
