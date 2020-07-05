package poussecafe.source.pcmil;

import java.util.Optional;

public class FormattedPcMilTokenStreamBuilder {

    private StringBuilder builder = new StringBuilder();

    public void resetIndent() {
        indent = "";
    }

    private String indent = "";

    public void appendNewLine() {
        builder.append('\n');
    }

    public void indent() {
        builder.append(indent);
    }

    public void incrementIndent() {
        indent = indent + TAB;
    }

    private static final Object TAB = "    ";

    @Override
    public String toString() {
        return builder.toString();
    }

    public void appendCommandToken(String name) {
        builder.append(name);
        builder.append('?');
    }

    public void appendDomainEventToken(String name) {
        builder.append(name);
        builder.append('!');
    }

    public void appendEndOfConsumptionToken(Optional<String> note) {
        builder.append(" -> .");
        if(note.isPresent()) {
            builder.append(" [");
            builder.append(note.get());
            builder.append(']');
        }
    }

    public void appendConsumptionToken() {
        builder.append(" -> ");
    }

    public void appendFactoryToken(String aggregateName) {
        builder.append(aggregateName);
        builder.append("Factory");
    }

    public void appendRepositoryToken(String aggregateName) {
        builder.append(aggregateName);
        builder.append("Repository");
    }

    public void appendRunnerToken(String aggregateName) {
        builder.append(aggregateName);
    }

    public void appendAggregateListener(String aggregateName, String listenerName) {
        builder.append("@").append(aggregateName);
        builder.append('[').append(listenerName).append(']');
    }

    public void appendOpenRelation() {
        builder.append(':');
    }

    public void appendCloseRelation() {
        builder.append(':');
    }

    public void appendOptionalEventToken() {
        builder.append('#');
    }
}
