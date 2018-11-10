package poussecafe.storage;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.EntityData;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DataAccessImplementation {

    String storageName();

    Class<? extends EntityData<?>> dataImplementation();

    Class<? extends AggregateRoot<?, ?>> aggregateRoot();

}
