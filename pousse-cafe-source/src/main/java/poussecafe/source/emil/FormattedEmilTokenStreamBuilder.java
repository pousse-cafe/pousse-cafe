package poussecafe.source.emil;

import java.util.Optional;

public class FormattedEmilTokenStreamBuilder {

    private StringBuilder builder = new StringBuilder();

    public void appendHeader(Optional<String> processName) {
        builder.append("process ");
        if(processName.isPresent()) {
            builder.append(processName.get());
        } else {
            builder.append('*');
        }
        appendNewLine();
        appendNewLine();
    }

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

    private static final String TAB = "    ";

    @Override
    public String toString() {
        return builder.toString();
    }

    public void appendCommandIdentifier(String name) {
        builder.append(name);
        builder.append('?');
    }

    public void appendDomainEventIdentifier(String name) {
        builder.append(name);
        builder.append('!');
    }

    public void appendEndOfConsumption() {
        builder.append(" -> .");
    }

    public void appendOpeningNote(String note) {
        appendInlineNote(note);
        builder.append(' ');
    }

    public void appendInlineNote(String note) {
        builder.append('[');
        builder.append(note);
        builder.append(']');
    }

    public void appendClosingNote(String note) {
        builder.append(' ');
        appendInlineNote(note);
    }

    public void appendOpeningConsumptionToken() {
        builder.append(" ->");
    }

    public void appendConsumptionToken() {
        builder.append(" -> ");
    }

    public void appendClosingConsumptionToken() {
        builder.append("-> ");
    }

    public void appendFactoryIdentifier(String className) {
        builder.append("F{");
        builder.append(className);
        builder.append('}');
    }

    public void appendRepositoryIdentifier(String className) {
        builder.append("Re{");
        builder.append(className);
        builder.append('}');
    }

    public void appendRunnerIdentifier(String className) {
        builder.append("Ru{");
        builder.append(className);
        builder.append('}');
    }

    public void appendAggregateIdentifier(String aggregateName) {
        builder.append("@").append(aggregateName);
    }

    public void appendOpenRelation() {
        builder.append(':');
    }

    public void appendCloseRelation() {
        builder.append(':');
    }

    public void appendOptionalOperator() {
        builder.append('#');
    }

    public void appendSeveralOperator() {
        builder.append("+");
    }

    public void appendProcessIdentifier(String processName) {
        builder.append("P{");
        builder.append(processName);
        builder.append('}');
    }

    public void decrementIndent() {
        indent = indent.substring(0, indent.length() - TAB.length());
    }
}
