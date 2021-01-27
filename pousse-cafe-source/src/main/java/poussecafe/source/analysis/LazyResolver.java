package poussecafe.source.analysis;

import java.util.Optional;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

public class LazyResolver {

    public LazyResolver(Supplier<Optional<ResolvedClass>> resolver) {
        requireNonNull(resolver);
        this.resolver = resolver;
    }

    public Optional<ResolvedClass> resolve() {
        if(!resolved) {
            resolved = true;
            resolvedTypeName = resolver.get();
        }
        return resolvedTypeName;
    }

    private boolean resolved;

    private Optional<ResolvedClass> resolvedTypeName;

    private Supplier<Optional<ResolvedClass>> resolver;
}
