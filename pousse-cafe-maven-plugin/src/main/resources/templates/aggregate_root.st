package <modelPackage>;

import poussecafe.contextconfigurer.Aggregate;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.EntityData;
<if(demoAttribute)>import poussecafe.property.Property;<endif>

@Aggregate(
    factory = <name>Factory.class,
    repository = <name>Repository.class
)
public class <name> extends AggregateRoot\<<name>Key, <name>.Data> {

    public static interface Data extends EntityData\<<name>Key> {

<if(demoAttribute)>
        Property\<String> name();
<endif>
    }
}
