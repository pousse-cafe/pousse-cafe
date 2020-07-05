package poussecafe.source.pcmil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import poussecafe.source.model.Message;
import poussecafe.source.model.MessageListener;
import poussecafe.source.model.MessageListenerContainerType;
import poussecafe.source.model.MessageType;
import poussecafe.source.model.ProducedEvent;

import static java.util.Objects.requireNonNull;
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
        appendMessageConsumption(rootListener.consumedMessage());
    }

    private void appendMessageConsumption(Message message) {
        appendMessage(message);

        List<MessageListener> consumers = findAndRemoveConsumers(message);
        if(consumers.isEmpty()) {
            appendNoListener();
        } else if(consumers.size() == 1) {
            appendSingleListener(consumers.get(0));
        } else {
            appendMultipleListeners(consumers);
        }
    }

    private void appendMessage(Message message) {
        if(message.type() == MessageType.COMMAND) {
            builder.appendCommandToken(message.name());
        } else if(message.type() == MessageType.DOMAIN_EVENT) {
            builder.appendDomainEventToken(message.name());
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

    private void appendNoListener() {
        builder.appendEndOfConsumptionToken(Optional.empty());
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
            builder.appendEndOfConsumptionToken(Optional.of("external"));
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
        builder.appendFactoryToken(listener.container().aggregateName().orElseThrow());
        builder.appendNewLine();
    }

    private void appendMessageConsumptions(MessageListener listener) {
        for(ProducedEvent producedEvent : listener.producedEvents()) {
            appendMessageConsumption(producedEvent.message());
        }
    }

    private void appendRepositoryListener(MessageListener listener) {
        appendRepositoryName(listener);
        appendMessageConsumptions(listener);
    }

    private void appendRepositoryName(MessageListener listener) {
        builder.appendRepositoryToken(listener.container().aggregateName().orElseThrow());
        builder.appendNewLine();
    }

    private void appendAggregateRootListener(MessageListener listener) {
        appendRunnerAndAggregateRoot(listener);
        if(!listener.producedEvents().isEmpty()) {
            appendAggregateMessageConsumptions(listener);
        } else {
            builder.appendNewLine();
        }
    }

    private void appendRunnerAndAggregateRoot(MessageListener listener) {
        builder.appendRunnerToken(listener.runnerName().orElse(""));
        builder.appendNewLine();
        builder.incrementIndent();
        builder.indent();
        builder.appendAggregateListener(listener.container().aggregateName().orElseThrow(), listener.methodName());
    }

    private void appendAggregateMessageConsumptions(MessageListener listener) {
        builder.appendOpenRelation();
        builder.appendNewLine();
        builder.incrementIndent();
        for(ProducedEvent producedEvent : listener.producedEvents()) {
            builder.indent();
            builder.appendCloseRelation();
            if(!producedEvent.required()) {
                builder.appendOptionalEventToken();
            }
            appendMessageConsumption(producedEvent.message());
        }
    }

    public static class Builder {

        private PcMilExporter exporter = new PcMilExporter();

        public PcMilExporter build() {
            requireNonNull(exporter.listeners);
            return exporter;
        }

        public Builder withListeners(List<MessageListener> listeners) {
            exporter.listeners = new LinkedList<>(listeners);
            exporter.listeners.sort(new PcMilListenersComparator());
            exporter.producedEvents = new HashSet<>();
            listeners.stream().forEach(listener -> exporter.producedEvents.addAll(
                    listener.producedEvents().stream().map(ProducedEvent::message).collect(toList())));
            return this;
        }
    }

    private PcMilExporter() {

    }
}
