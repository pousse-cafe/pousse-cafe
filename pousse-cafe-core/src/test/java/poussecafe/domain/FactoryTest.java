package poussecafe.domain;

import org.junit.Test;
import poussecafe.exception.AssertionFailedException;
import poussecafe.util.ReflectionUtils;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public abstract class FactoryTest<K, D extends EntityAttributes<K>, A extends AggregateRoot<K, D>, F extends Factory<K, A, D>> {

    private EntityFactory primitiveFactory = mock(EntityFactory.class);

    private K givenKey;

    private A createdEntity;

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
        F factory = factory();
        ReflectionUtils.access(factory).set("componentFactory", primitiveFactory);
        when(primitiveFactory.newEntity(any())).thenReturn(givenKey);
        createdEntity = factory.newAggregateWithKey(givenKey);
    }

    protected abstract Class<K> keyClass();

    protected abstract F factory();

    private void thenCreatedAggregateHasKey() {
        assertThat(createdEntity.attributes().key(), is(givenKey));
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
