package poussecafe.runtime;

public class ConsumptionIdGenerator {

    public ConsumptionIdGenerator(String prefix) {
        this.prefix = prefix;
    }

    private String prefix;

    private int sequenceNumber;

    public synchronized String next() {
        StringBuilder builder = new StringBuilder(prefix);
        builder.append('_');
        builder.append(sequenceNumber++);
        return builder.toString();
    }

    public String prefix() {
        return prefix;
    }
}
