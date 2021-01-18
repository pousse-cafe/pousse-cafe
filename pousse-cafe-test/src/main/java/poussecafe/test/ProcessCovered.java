package poussecafe.test;

import java.lang.annotation.Target;
import poussecafe.discovery.DefaultProcess;
import poussecafe.domain.Process;

import static java.lang.annotation.ElementType.TYPE;

@Target(TYPE)
public @interface ProcessCovered {

    /**
     * @deprecated use value instead
     */
    @Deprecated(since = "0.24")
    Class<? extends Process> process() default DefaultProcess.class;

    Class<? extends Process> value();
}
