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
        builder = new StringBuilder();
        while(!listeners.isEmpty()) {
            MessageListener next = next();
            appendTopListener(next);

            listeners.remove(next);
            if(!listeners.isEmpty()) {
                builder.append('\n');
            }
        }
        pcMil = builder.toString();
    }

    private StringBuilder builder;

    private List<MessageListener> listeners;

    private MessageListener next() {
        Optional<MessageListener> rootListener = findTopListener(listeners);
        if(rootListener.isEmpty()) {
            return listeners.get(0);
        } else {
            return rootListener.get();
        }
    }

    private Optional<MessageListener> findTopListener(List<MessageListener> listeners) {
        return listeners.stream()
                .filter(listener -> !producedEvents.contains(listener.consumedMessage()))
                .findFirst();
    }

    private Set<Message> producedEvents = new HashSet<>();

    private void appendTopListener(MessageListener rootListener) {
        Message message = rootListener.consumedMessage();
        appendMessageConsumption(0, message);
    }

    private void appendMessageConsumption(int level, Message message) {
        appendMessage(message);

        List<MessageListener> consumers = findAndRemoveConsumers(message);
        if(consumers.isEmpty()) {
            appendNoListener();
        } else if(consumers.size() == 1) {
            appendSingleListener(level, consumers.get(0));
        } else {
            appendMultipleListeners(level, consumers);
        }
    }

    private void appendMessage(Message message) {
        builder.append(message.name());
        if(message.type() == MessageType.COMMAND) {
            builder.append('?');
        } else if(message.type() == MessageType.DOMAIN_EVENT) {
            builder.append('!');
        }
    }

    private void appendNoListener() {
        builder.append(" -> .").append('\n');
    }

    private void appendSingleListener(int level, MessageListener consumer) {
        builder.append(" -> ");
        appendListener(level, consumer);
    }

    private void appendMultipleListeners(int level, List<MessageListener> consumers) {
        builder.append(" -> ").append('\n');
        for(MessageListener listener : consumers) {
            builder.append(indent(level + 1)).append(" -> ");
            appendListener(level, listener);
        }
    }

    private void appendListener(int level, MessageListener listener) {
        if(listener.container().type() == MessageListenerContainerType.FACTORY) {
            appendFactoryListener(level, listener);
        } else if(listener.container().type() == MessageListenerContainerType.REPOSITORY) {
            appendRepositoryListener(level, listener);
        } else if(listener.container().type() == MessageListenerContainerType.ROOT) {
            appendAggregateRootListener(level, listener);
        } else {
            builder.append(". [external]");
        }
    }

    private void appendFactoryListener(int level, MessageListener listener) {
        appendFactoryName(listener);
        appendMessageConsumptions(level, listener);
    }

    private void appendFactoryName(MessageListener listener) {
        builder.append(listener.container().aggregateName().orElseThrow());
        builder.append("Factory").append('\n');
    }

    private void appendMessageConsumptions(int level, MessageListener listener) {
        for(ProducedEvent producedEvent : listener.producedEvents()) {
            appendMessageConsumption(level, producedEvent.message());
        }
    }

    private void appendRepositoryListener(int level, MessageListener listener) {
        appendRepositoryName(listener);
        appendMessageConsumptions(level, listener);
    }

    private void appendRepositoryName(MessageListener listener) {
        builder.append(listener.container().aggregateName().orElseThrow());
        builder.append("Repository").append('\n');
    }

    private void appendAggregateRootListener(int level, MessageListener listener) {
        appendRunnerAndAggregateRoot(level, listener);
        appendAggregateMessageConsumptions(level, listener);
    }

    private void appendRunnerAndAggregateRoot(int level, MessageListener listener) {
        appendRunnerName(listener);
        appendAggregateListener(level, listener);
    }

    private void appendRunnerName(MessageListener listener) {
        builder.append(listener.runnerName().orElse("")).append('\n');
    }

    private void appendAggregateListener(int level, MessageListener listener) {
        builder.append(indent(level + 1)).append("@").append(listener.container().aggregateName().orElseThrow())
            .append('[').append(listener.methodName()).append(']')
            .append(':').append('\n');
    }

    private void appendAggregateMessageConsumptions(int level, MessageListener listener) {
        for(ProducedEvent producedEvent : listener.producedEvents()) {
            builder.append(indent(level + 2)).append(':');
            if(!producedEvent.required()) {
                builder.append('#');
            }
            appendMessageConsumption(level + 2, producedEvent.message());
        }
    }

    private String indent(int level) {
        var builder = new StringBuilder();
        for(int i = 0; i < level; ++i) {
            builder.append(TAB);
        }
        return builder.toString();
    }

    private static final Object TAB = "    ";

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
