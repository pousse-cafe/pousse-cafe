package <modelPackage>;

import poussecafe.domain.Factory;

public class <name>Factory extends Factory\<<name>Key, <name>, <name>.Data> {

    public <name> new<name>(Parameters parameters) {
        <name> aggregate = newAggregateWithKey(parameters.key);
        aggregate.name().let(this).set(parameters.name);
        return aggregate;
    }

    public static class Parameters {

        public <name>Key key;

        public String name;
    }
}
