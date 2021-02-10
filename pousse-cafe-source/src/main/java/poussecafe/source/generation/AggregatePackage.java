package poussecafe.source.generation;

import static java.util.Objects.requireNonNull;

public class AggregatePackage {

    public String packageName() {
        return packageName;
    }

    private String packageName;

    public String aggregateName() {
        return aggregateName;
    }

    private String aggregateName;

    public AggregatePackage(String packageName, String aggregateName) {
        requireNonNull(packageName);
        this.packageName = packageName;

        requireNonNull(aggregateName);
        this.aggregateName = aggregateName;
    }
}
