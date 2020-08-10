package poussecafe.source.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import poussecafe.source.analysis.ResolvedTypeName;
import poussecafe.source.analysis.CompilationUnitResolver;

import static java.util.Objects.requireNonNull;
import static poussecafe.util.Equality.referenceEquals;

public class Message {

    public static Message ofTypeName(ResolvedTypeName typeName) {
        if(!isMessage(typeName)) {
            throw new IllegalArgumentException("Given type is not a message");
        }
        return new Message.Builder()
                .name(typeName.simpleName())
                .type(typeOf(typeName))
                .build();
    }

    public static boolean isMessage(ResolvedTypeName typeName) {
        return typeName.instanceOf(CompilationUnitResolver.MESSAGE_CLASS);
    }

    private static MessageType typeOf(ResolvedTypeName typeName) {
        if(typeName.instanceOf(CompilationUnitResolver.DOMAIN_EVENT_INTERFACE)) {
            return MessageType.DOMAIN_EVENT;
        } else if(typeName.instanceOf(CompilationUnitResolver.COMMAND_INTERFACE)) {
            return MessageType.COMMAND;
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public static Message command(String name) {
        return new Message.Builder()
                .name(name)
                .type(MessageType.COMMAND)
                .build();
    }

    public static Message domainEvent(String name) {
        return new Message.Builder()
                .name(name)
                .type(MessageType.DOMAIN_EVENT)
                .build();
    }

    public String name() {
        return name;
    }

    private String name;

    public MessageType type() {
        return type;
    }

    private MessageType type;

    public static class Builder {

        private Message message = new Message();

        public Message build() {
            requireNonNull(message.name);
            requireNonNull(message.type);
            return message;
        }

        public Builder name(String name) {
            message.name = name;
            return this;
        }

        public Builder type(MessageType type) {
            message.type = type;
            return this;
        }
    }

    private Message() {

    }

    @Override
    public boolean equals(Object obj) {
        return referenceEquals(this, obj).orElse(other -> new EqualsBuilder()
                .append(name, other.name)
                .append(type, other.type)
                .build());
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(name)
                .append(type)
                .build();
    }

    @Override
    public String toString() {
        var builder = new StringBuilder();
        builder.append(name);
        if(type == MessageType.COMMAND) {
            builder.append('?');
        } else {
            builder.append('!');
        }
        return builder.toString();
    }
}
