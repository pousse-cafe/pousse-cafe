package poussecafe.messaging;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target(METHOD)
public @interface CommandListener {

    String id() default "";

    String source() default Queue.DEFAULT_COMMAND_QUEUE_NAME;
}
