package poussecafe.test;

import java.lang.annotation.Target;
import poussecafe.domain.Process;

import static java.lang.annotation.ElementType.TYPE;

@Target(TYPE)
public @interface ProcessCovered {

    Class<? extends Process> process();
}
