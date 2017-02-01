package poussecafe.storable;

import org.junit.Test;
import poussecafe.exception.AssertionFailedException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public abstract class ActiveStorableFactoryTest<K, D extends StorableData<K>, A extends ActiveStorable<K, D>, F extends ActiveStorableFactory<K, A, D>> {

    private K givenKey;

    private A createdStorable;

    @Test
    public void factoryAtLeastSetsKey() {
        givenKey();
        whenCreatingAggregate();
        thenCreatedAggregateHasKey();
    }

    private void givenKey() {
        givenKey = buildKey();
    }

    protected abstract K buildKey();

    private void whenCreatingAggregate() {
        createdStorable = factory().newStorableWithKey(givenKey);
    }

    protected abstract F factory();

    private void thenCreatedAggregateHasKey() {
        assertThat(createdStorable.getKey(), is(givenKey));
    }

    @Test(expected = AssertionFailedException.class)
    public void creationFailsIfNoKey() {
        givenNoKey();
        whenCreatingAggregate();
    }

    private void givenNoKey() {
        givenKey = null;
    }
}
