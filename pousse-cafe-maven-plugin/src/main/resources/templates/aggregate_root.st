package <modelPackage>;

import poussecafe.domain.AggregateRoot;
import poussecafe.domain.EntityData;
import poussecafe.property.Property;

public class <name> extends AggregateRoot\<<name>Key, <name>.Data> {

    public Property\<String> name() {
        return getData().name();
    } 

    public static interface Data extends EntityData\<<name>Key> {

        Property\<String> name();
    }
}
