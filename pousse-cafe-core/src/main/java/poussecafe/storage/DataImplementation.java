package poussecafe.storage;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import poussecafe.domain.Entity;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DataImplementation {

    String[] storageNames() default {};

    Class<? extends Entity<?, ?>> entity();

}
