package poussecafe.attribute.entity;

import org.junit.Test;
import poussecafe.entity.SimpleEntity;
import poussecafe.environment.EntityFactory;
import poussecafe.runtime.ActiveAggregate;
import poussecafe.testmodule.SimpleAggregate;
import poussecafe.testmodule.SimpleEntityData;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EntityAttributeTest {

    @Test(expected = NullPointerException.class)
    public void cannotSetEmptyEntity() {
        givenAggregate();
        givenAttribute();
        whenSettingEntity(null);
    }

    private void givenAttribute() {
        attribute = EntityAttributeBuilder.entity(SimpleEntity.class, SimpleEntityData.class)
            .read(this::getter)
            .write(this::setter)
            .build();
    }

    private void givenAggregate() {
        aggregate = new SimpleAggregate();
        EntityFactory entityFactory = mock(EntityFactory.class);
        when(entityFactory.newEntity(any())).thenReturn(new SimpleAggregate());
        aggregate.entityFactory(entityFactory);

        ActiveAggregate.instance().set(aggregate);
    }

    private void whenSettingEntity(SimpleEntity value) {
        attribute.value(value);
    }

    private SimpleAggregate aggregate;

    private EntityAttribute<SimpleEntity> attribute;

    private SimpleEntityData getter() {
        return data;
    }

    private SimpleEntityData data;

    private void setter(SimpleEntityData data) {
        this.data = data;
    }

    @Test
    public void canSetPresentEntity() {
        givenAggregate();
        givenAttribute();
        whenSettingEntity(presentEntity());
        thenEntityPresent();
    }

    private SimpleEntity presentEntity() {
        SimpleEntity entity = new SimpleEntity();
        entity.attributes(new SimpleEntityData());
        return entity;
    }

    private void thenEntityPresent() {
        assertThat(data, notNullValue());
    }
}
