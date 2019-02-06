package <modelPackage>;

import poussecafe.domain.Factory;

public class <name>Factory extends Factory\<<name>Key, <name>, <name>.Attributes> {

    public <name> new<name>(Parameters parameters) {
        <name> aggregate = newAggregateWithKey(parameters.key);
        <if(demoAttribute)>
        aggregate.attributes().name().value(parameters.name);
        <endif>
        return aggregate;
    }

    public static class Parameters {

        public <name>Key key;
<if(demoAttribute)>

        public String name;
<endif>
    }
}
