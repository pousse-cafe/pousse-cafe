package poussecafe.attribute.entity;

import java.util.Optional;
import org.junit.Test;
import poussecafe.entity.SimpleEntity;
import poussecafe.environment.EntityFactory;
import poussecafe.runtime.ActiveAggregate;
import poussecafe.testmodule.SimpleAggregate;
import poussecafe.testmodule.SimpleEntityData;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OptionalEntityAttributeTest {

    @Test
    public void canSetEmptyEntity() {
        givenAggregate();
        givenAttribute();
        whenSettingEntity(Optional.empty());
        thenNoEntityPresent();
    }

    private void givenAttribute() {
        attribute = EntityAttributeBuilder.optional(SimpleEntity.class, SimpleEntityData.class)
            .read(this::getter)
            .write(this::setter)
            .build();
    }

    private void givenAggregate() {
        aggregate = new SimpleAggregate();
        EntityFactory entityFactory = mock(EntityFactory.class);
        when(entityFactory.newEntity(any())).thenReturn(new SimpleAggregate());
        aggregate.entityFactory(entityFactory);
    }

    private void whenSettingEntity(Optional<SimpleEntity> value) {
        attribute.value(value);
    }

    private SimpleAggregate aggregate;

    private OptionalEntityAttribute<SimpleEntity> attribute;

    private SimpleEntityData getter() {
        return data;
    }

    private SimpleEntityData data;

    private void setter(SimpleEntityData data) {
        this.data = data;
    }

    private void thenNoEntityPresent() {
        assertThat(data, nullValue());
        assertTrue(attribute.value().isEmpty());
    }

    @Test
    public void canSetPresentEntity() {
        givenAggregate();
        givenRegisteredAggregate();
        givenAttribute();
        whenSettingEntity(Optional.of(presentEntity()));
        thenEntityPresent();
    }

    private void givenRegisteredAggregate() {
        ActiveAggregate.instance().set(aggregate);
    }

    private SimpleEntity presentEntity() {
        SimpleEntity entity = new SimpleEntity();
        entity.attributes(new SimpleEntityData());
        return entity;
    }

    private void thenEntityPresent() {
        assertThat(data, notNullValue());
        assertTrue(attribute.value().isPresent());
    }
}
