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
        var builder = new StringBuilder();
        while(!listeners.isEmpty()) {
            MessageListener next = next();
            appendTopListener(builder, next);

            listeners.remove(next);
            if(!listeners.isEmpty()) {
                builder.append('\n');
            }
        }
        pcMil = builder.toString();
    }

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

    private void appendTopListener(StringBuilder builder, MessageListener rootListener) {
        appendListeners(0, builder, rootListener.consumedMessage(), findAndRemoveListeners(rootListener.consumedMessage()));
    }

    private void appendListeners(int level, StringBuilder builder, Message message, List<MessageListener> listeners) {
        appendMessage(builder, message);

        if(listeners.isEmpty()) {
            builder.append(" -> .").append('\n');
        } else if(listeners.size() == 1) {
            builder.append(" -> ");
        } else {
            builder.append(" -> ").append('\n');
        }

        for(MessageListener listener : listeners) {
            if(listeners.size() > 1) {
                builder.append(indent(level + 1)).append(" -> ");
            }
            appendListener(level, builder, listener);
        }
    }

    private void appendMessage(StringBuilder builder, Message message) {
        builder.append(message.name());
        if(message.type() == MessageType.COMMAND) {
            builder.append('?');
        } else if(message.type() == MessageType.DOMAIN_EVENT) {
            builder.append('!');
        }
    }

    private void appendListener(int level, StringBuilder builder, MessageListener listener) {
        if(listener.container().type() == MessageListenerContainerType.FACTORY) {
            appendFactoryListener(level, builder, listener);
        } else if(listener.container().type() == MessageListenerContainerType.REPOSITORY) {
            appendRepositoryListener(level, builder, listener);
        } else if(listener.container().type() == MessageListenerContainerType.ROOT) {
            appendAggregateRootListener(level, builder, listener);
        } else {
            builder.append(". [external]");
        }
    }

    private void appendFactoryListener(int level, StringBuilder builder, MessageListener listener) {
        builder.append(listener.container().aggregateName().orElseThrow());
        builder.append("Factory").append('\n');
        for(ProducedEvent producedEvent : listener.producedEvents()) {
            appendListeners(level, builder, producedEvent.message(), findAndRemoveListeners(producedEvent.message()));
        }
    }

    private void appendRepositoryListener(int level, StringBuilder builder, MessageListener listener) {
        builder.append(listener.container().aggregateName().orElseThrow());
        builder.append("Repository").append('\n');
        for(ProducedEvent producedEvent : listener.producedEvents()) {
            appendListeners(level, builder, producedEvent.message(), findAndRemoveListeners(producedEvent.message()));
        }
    }

    private void appendAggregateRootListener(int level, StringBuilder builder, MessageListener listener) {
        builder.append(listener.runnerName().orElse("")).append('\n');
        builder.append(indent(level + 1)).append("@").append(listener.container().aggregateName().orElseThrow())
            .append('[').append(listener.methodName()).append(']')
            .append(':').append('\n');
        for(ProducedEvent producedEvent : listener.producedEvents()) {
            builder.append(indent(level + 2)).append(':');
            appendListeners(level + 2, builder, producedEvent.message(), findAndRemoveListeners(producedEvent.message()));
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

    private List<MessageListener> findAndRemoveListeners(Message event) {
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
