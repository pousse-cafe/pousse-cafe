package <modelPackage>;

import poussecafe.domain.Factory;

public class <name>Factory extends Factory\<<name>Id, <name>, <name>.Attributes> {

    public <name> new<name>(Parameters parameters) {
        <name> aggregate = newAggregateWithId(parameters.id);
        <if(demoAttribute)>
        aggregate.attributes().name().value(parameters.name);
        <endif>
        return aggregate;
    }

    public static class Parameters {

        public <name>Id id;
<if(demoAttribute)>

        public String name;
<endif>
    }
}
