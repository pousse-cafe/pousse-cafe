package <modelPackage>;

import poussecafe.context.Aggregate;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.EntityData;
import poussecafe.property.Property;
import poussecafe.property.ProtectedProperty;
import poussecafe.property.ProtectedPropertyBuilder;

@Aggregate(
    factory = <name>Factory.class,
    repository = <name>Repository.class
)
public class <name> extends AggregateRoot\<<name>Key, <name>.Data> {

    public ProtectedProperty\<String> name() {
        return ProtectedPropertyBuilder.protect(data().name()).of(this)
                .allowClassWrite(<name>Factory.class)
                .build();
    } 

    public static interface Data extends EntityData\<<name>Key> {

        Property\<String> name();
    }
}
