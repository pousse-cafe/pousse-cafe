package poussecafe.source.analysis;

import java.util.Optional;
import java.util.function.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.util.Objects.requireNonNull;

public class LazyResolver {

    public LazyResolver(String name, Supplier<Optional<ResolvedClass>> resolver) {
        requireNonNull(name);
        this.name = name;

        requireNonNull(resolver);
        this.resolver = resolver;
    }

    private String name;

    public synchronized Optional<ResolvedClass> resolve() {
        if(resolving) {
            throw new IllegalStateException("Resolution is looping for name " + name);
        }
        if(!resolved) {
            try {
                resolving = true;
                resolvedTypeName = resolver.get();
                resolved = true;
            } catch(Exception e) {
                logger.debug("Unable to lazy resolve", e);
                resolvedTypeName = Optional.empty();
                resolved = true;
            } finally {
                resolving = false;
            }
        }
        return resolvedTypeName;
    }

    private Logger logger = LoggerFactory.getLogger(getClass());

    private boolean resolving;

    private boolean resolved;

    private Optional<ResolvedClass> resolvedTypeName;

    private Supplier<Optional<ResolvedClass>> resolver;
}
