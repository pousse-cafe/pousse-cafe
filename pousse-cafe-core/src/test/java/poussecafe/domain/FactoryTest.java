package poussecafe.domain;

import org.junit.Test;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.Factory;
import poussecafe.exception.AssertionFailedException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public abstract class FactoryTest<K, D extends EntityData<K>, A extends AggregateRoot<K, D>, F extends Factory<K, A, D>> {

    private ComponentFactory primitiveFactory = mock(ComponentFactory.class);

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
        factory.setComponentFactory(primitiveFactory);
        when(primitiveFactory.newComponent(any())).thenReturn(givenKey);
        createdEntity = factory.newAggregateWithKey(givenKey);
    }

    protected abstract Class<K> keyClass();

    protected abstract F factory();

    private void thenCreatedAggregateHasKey() {
        assertThat(createdEntity.getKey(), is(givenKey));
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
