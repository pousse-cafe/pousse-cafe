package poussecafe.source.pcmil;

import java.util.HashSet;
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
            Optional<MessageListener> rootListener = findRootListener(listeners);
            if(rootListener.isEmpty()) {
                rootListener = Optional.of(listeners.get(0));
            }

            builder.append(rootListener.orElseThrow().consumedMessageName());
            builder.append(" -> ");

            if(rootListener.orElseThrow().container().type() == MessageListenerContainerType.FACTORY) {
                builder.append(rootListener.orElseThrow().container().aggregateName().orElseThrow());
                builder.append("Factory").append('\n');
            } else if(rootListener.orElseThrow().container().type() == MessageListenerContainerType.REPOSITORY) {
                builder.append(rootListener.orElseThrow().container().aggregateName().orElseThrow());
                builder.append("Repository").append('\n');
            } else if(rootListener.orElseThrow().container().type() == MessageListenerContainerType.ROOT) {
                builder.append("[TODO runner]").append('\n');
                builder.append("    @").append(rootListener.orElseThrow().container().aggregateName().orElseThrow())
                    .append('[').append(rootListener.orElseThrow().methodName()).append(']')
                    .append(':').append('\n');
                for(ProducedEvent producedEvent : rootListener.orElseThrow().producedEvents()) {
                    builder.append("        :").append(producedEvent.eventName()).append('!').append(" -> . [TODO chain]").append('\n');
                }
            } else {
                builder.append(". [external]");
            }

            listeners.remove(rootListener.orElseThrow());
            if(!listeners.isEmpty()) {
                builder.append('\n');
            }
        }
        pcMil = builder.toString();
    }

    private List<MessageListener> listeners;

    private Optional<MessageListener> findRootListener(List<MessageListener> listeners) {
        return listeners.stream()
                .filter(listener -> !producedEvents.contains(listener.consumedMessageName()))
                .findFirst();
    }

    private Set<String> producedEvents = new HashSet<>();

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
