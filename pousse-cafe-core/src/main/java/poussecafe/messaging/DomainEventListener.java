package poussecafe.messaging;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target(METHOD)
public @interface DomainEventListener {

    String id() default "";

    String source() default Queue.DEFAULT_DOMAIN_EVENT_QUEUE_NAME;
}
