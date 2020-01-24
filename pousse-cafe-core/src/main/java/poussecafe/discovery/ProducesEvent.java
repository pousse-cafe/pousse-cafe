package poussecafe.discovery;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import poussecafe.domain.DomainEvent;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


@Retention(RUNTIME)
@Target(METHOD)
@Repeatable(ProducesEvents.class)
public @interface ProducesEvent {

    Class<? extends DomainEvent> event();

    boolean required() default true;
}
