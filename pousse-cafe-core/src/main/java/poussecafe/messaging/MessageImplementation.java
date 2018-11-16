package poussecafe.messaging;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import poussecafe.messaging.internal.InternalMessaging;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface MessageImplementation {

    Class<? extends Message> message();

    String[] messagingNames() default InternalMessaging.NAME;
}
