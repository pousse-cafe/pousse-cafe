package poussecafe.contextconfigurer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.EntityAttributes;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DataAccessImplementation {

    String storageName();

    Class<? extends EntityAttributes<?>> dataImplementation();

    Class<? extends AggregateRoot<?, ?>> aggregateRoot();

}
