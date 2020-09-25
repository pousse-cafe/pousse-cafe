package poussecafe.discovery;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @deprecated module's scope is defined by its base package makes this redundant
 */
@Deprecated(since = "0.24")
@Retention(RUNTIME)
@Target(TYPE)
public @interface Module {

    Class<? extends poussecafe.domain.Module> value();
}
