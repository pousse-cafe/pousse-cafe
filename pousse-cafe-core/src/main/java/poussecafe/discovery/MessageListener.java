package poussecafe.discovery;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import poussecafe.environment.AggregateMessageListenerRunner;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target(METHOD)
public @interface MessageListener {

    String id() default "";

    Class<? extends AggregateMessageListenerRunner<?, ?, ?>> runner() default VoidAggregateMessageListenerRunner.class;

    String collisionSpace() default "";

    String shortId() default "";

    String customStep() default "";
}
