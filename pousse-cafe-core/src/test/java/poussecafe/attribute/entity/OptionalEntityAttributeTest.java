package poussecafe.attribute.entity;

import java.util.Optional;
import org.junit.Test;
import poussecafe.domain.SimpleAggregate;
import poussecafe.domain.SimpleAggregateData;
import poussecafe.environment.EntityFactory;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OptionalEntityAttributeTest {

    @Test
    public void canSetEmptyEntity() {
        givenAttribute();
        whenSettingEntity(Optional.empty());
        thenNoEntityPresent();
    }

    private void givenAttribute() {
        attribute = EntityAttributeBuilder.optional(SimpleAggregate.class, SimpleAggregateData.class)
            .read(this::getter)
            .write(this::setter)
            .build();

        aggregate = new SimpleAggregate();
        EntityFactory entityFactory = mock(EntityFactory.class);
        when(entityFactory.newEntity(any())).thenReturn(new SimpleAggregate());
        aggregate.entityFactory(entityFactory);
    }

    private void whenSettingEntity(Optional<SimpleAggregate> value) {
        attribute.inContextOf(aggregate).value(value);
    }

    private SimpleAggregate aggregate;

    private OptionalEntityAttribute<SimpleAggregate> attribute;

    private SimpleAggregateData getter() {
        return data;
    }

    private SimpleAggregateData data;

    private void setter(SimpleAggregateData data) {
        this.data = data;
    }

    private void thenNoEntityPresent() {
        assertThat(data, nullValue());
        assertTrue(attribute.inContextOf(aggregate).value().isEmpty());
    }

    @Test
    public void canSetPresentEntity() {
        givenAttribute();
        whenSettingEntity(Optional.of(presentEntity()));
        thenEntityPresent();
    }

    private SimpleAggregate presentEntity() {
        SimpleAggregate entity = new SimpleAggregate();
        entity.attributes(new SimpleAggregateData());
        return entity;
    }

    private void thenEntityPresent() {
        assertThat(data, notNullValue());
        assertTrue(attribute.inContextOf(aggregate).value().isPresent());
    }
}
