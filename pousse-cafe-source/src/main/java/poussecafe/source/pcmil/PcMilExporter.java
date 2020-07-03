package poussecafe.source.pcmil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import poussecafe.source.model.MessageListener;
import poussecafe.source.model.MessageListenerContainerType;
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
                .filter(listener -> !producedEvents.contains(listener.consumedMessageName()))
                .findFirst();
    }

    private Set<String> producedEvents = new HashSet<>();

    private void appendTopListener(StringBuilder builder, MessageListener rootListener) {
        appendListeners(0, builder, rootListener.consumedMessageName(), findAndRemoveListeners(rootListener.consumedMessageName()));
    }

    private void appendListeners(int level, StringBuilder builder, String messageName, List<MessageListener> isteners) {
        builder.append(messageName);

        if(isteners.isEmpty()) {
            builder.append(" -> .").append('\n');
        } else if(isteners.size() == 1) {
            builder.append(" -> ");
        } else {
            builder.append(" -> ").append('\n');
        }

        for(MessageListener listener : isteners) {
            if(isteners.size() > 1) {
                builder.append(indent(level + 1)).append(" -> ");
            }
            appendListener(level, builder, listener);
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
            appendListeners(level, builder, producedEvent.eventName() + "!", findAndRemoveListeners(producedEvent.eventName()));
        }
    }

    private void appendRepositoryListener(int level, StringBuilder builder, MessageListener listener) {
        builder.append(listener.container().aggregateName().orElseThrow());
        builder.append("Repository").append('\n');
        for(ProducedEvent producedEvent : listener.producedEvents()) {
            appendListeners(level, builder, producedEvent.eventName() + "!", findAndRemoveListeners(producedEvent.eventName()));
        }
    }

    private void appendAggregateRootListener(int level, StringBuilder builder, MessageListener listener) {
        builder.append('[').append(listener.runnerName().orElse("")).append(']').append('\n');
        builder.append(indent(level + 1)).append("@").append(listener.container().aggregateName().orElseThrow())
            .append('[').append(listener.methodName()).append(']')
            .append(':').append('\n');
        for(ProducedEvent producedEvent : listener.producedEvents()) {
            builder.append(indent(level + 2)).append(':');
            appendListeners(level + 2, builder, producedEvent.eventName() + "!", findAndRemoveListeners(producedEvent.eventName()));
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

    private List<MessageListener> findAndRemoveListeners(String eventName) {
        var removedListeners = new ArrayList<MessageListener>();
        Iterator<MessageListener> iterator = listeners.iterator();
        while(iterator.hasNext()) {
            MessageListener listener = iterator.next();
            if(listener.consumedMessageName().equals(eventName)) {
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
                    listener.producedEvents().stream().map(ProducedEvent::eventName).collect(toList())));
            return this;
        }
    }

    private PcMilExporter() {

    }
}
