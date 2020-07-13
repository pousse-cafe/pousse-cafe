package poussecafe.source.pcmil;

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

    public void appendConsumptionToken() {
        builder.append(" -> ");
    }

    public void appendFactoryIdentifier(String aggregateName) {
        builder.append(aggregateName);
        builder.append("Factory");
    }

    public void appendRepositoryIdentifier(String aggregateName) {
        builder.append(aggregateName);
        builder.append("Repository");
    }

    public void appendRunnerIdentifier(String aggregateName) {
        builder.append(aggregateName);
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
        builder.append("{}");
    }
}
