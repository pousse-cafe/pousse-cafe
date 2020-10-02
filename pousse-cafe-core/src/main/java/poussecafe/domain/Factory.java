package poussecafe.domain;

/**
 * @deprecated extend AggregateFactory instead
 */
@Deprecated(since = "0.24")
public abstract class Factory<K, A extends AggregateRoot<K, D>, D extends EntityAttributes<K>>
extends AggregateFactory<K, A, D> {

}
