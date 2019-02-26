package poussecafe.messaging;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import poussecafe.runtime.AggregateMessageListenerRunner;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target(METHOD)
public @interface DomainEventListener {

    String id() default "";

    Class<? extends AggregateMessageListenerRunner<?, ?, ?>> runner() default VoidAggregateMessageListenerRunner.class;
}
