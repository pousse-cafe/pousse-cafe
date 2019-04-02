package <modelPackage>;

import poussecafe.discovery.Aggregate;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.EntityAttributes;
<if(demoAttribute)>import poussecafe.attribute.Attribute;<endif>

@Aggregate(
    factory = <name>Factory.class,
    repository = <name>Repository.class
)
public class <name> extends AggregateRoot\<<name>Id, <name>.Attributes> {

    public static interface Attributes extends EntityAttributes\<<name>Id> {

<if(demoAttribute)>
        Attribute\<String> name();
<endif>
    }
}
