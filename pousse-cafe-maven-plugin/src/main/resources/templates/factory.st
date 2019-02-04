package <modelPackage>;

import poussecafe.domain.Factory;

public class <name>Factory extends Factory\<<name>Key, <name>, <name>.Data> {

    public <name> new<name>(Parameters parameters) {
        <name> aggregate = newAggregateWithKey(parameters.key);
        <if(demoAttribute)>
        aggregate.data().name().set(parameters.name);
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
