package poussecafe.test;

import java.lang.annotation.Target;
import poussecafe.domain.Process;

import static java.lang.annotation.ElementType.TYPE;

@Target(TYPE)
public @interface ProcessCovered {

    /**
     * @deprecated use value instead
     */
    @Deprecated(since = "0.24")
    Class<? extends Process> process();

    Class<? extends Process> value();
}
