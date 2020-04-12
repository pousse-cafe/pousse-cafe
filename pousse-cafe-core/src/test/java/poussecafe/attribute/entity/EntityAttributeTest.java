package poussecafe.attribute.entity;

import org.junit.Test;
import poussecafe.environment.EntityFactory;
import poussecafe.testmodule.SimpleAggregate;
import poussecafe.testmodule.SimpleAggregateData;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EntityAttributeTest {

    @Test(expected = NullPointerException.class)
    public void cannotSetEmptyEntity() {
        givenAttribute();
        whenSettingEntity(null);
    }

    private void givenAttribute() {
        attribute = EntityAttributeBuilder.entity(SimpleAggregate.class, SimpleAggregateData.class)
            .read(this::getter)
            .write(this::setter)
            .build();

        aggregate = new SimpleAggregate();
        EntityFactory entityFactory = mock(EntityFactory.class);
        when(entityFactory.newEntity(any())).thenReturn(new SimpleAggregate());
        aggregate.entityFactory(entityFactory);
    }

    private void whenSettingEntity(SimpleAggregate value) {
        attribute.inContextOf(aggregate).value(value);
    }

    private SimpleAggregate aggregate;

    private EntityAttribute<SimpleAggregate> attribute;

    private SimpleAggregateData getter() {
        return data;
    }

    private SimpleAggregateData data;

    private void setter(SimpleAggregateData data) {
        this.data = data;
    }

    @Test
    public void canSetPresentEntity() {
        givenAttribute();
        whenSettingEntity(presentEntity());
        thenEntityPresent();
    }

    private SimpleAggregate presentEntity() {
        SimpleAggregate entity = new SimpleAggregate();
        entity.attributes(new SimpleAggregateData());
        return entity;
    }

    private void thenEntityPresent() {
        assertThat(data, notNullValue());
    }
}
