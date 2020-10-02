package poussecafe.domain;

/**
 * @deprecated extend AggregateRepository instead
 */
@Deprecated(since = "0.24")
public abstract class Repository<A extends AggregateRoot<K, D>, K, D extends EntityAttributes<K>>
extends AggregateRepository<A, K, D> {

    /**
     * @deprecated use getOptional instead.
     */
    @Deprecated(since = "0.8.0")
    public A find(K id) {
        return getOptional(id).orElse(null);
    }

    /**
     * @deprecated use wrapNullable instead.
     */
    @Deprecated(since = "0.8.0")
    protected A wrap(D data) {
        return wrapNullable(data).orElse(null);
    }
}
