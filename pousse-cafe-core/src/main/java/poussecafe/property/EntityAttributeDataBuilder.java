package poussecafe.property;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;
import poussecafe.domain.Entity;
import poussecafe.domain.EntityAttributes;

public class EntityAttributeDataBuilder<D extends EntityAttributes<?>, E extends Entity<?, D>, F extends D> {

    EntityAttributeDataBuilder() {

    }

    Class<E> entityClass;

    Class<F> dataClass;

    public EntityAttributeDataBuilder<D, E, F> get(Supplier<F> getter) {
        Objects.requireNonNull(getter);
        this.getter = getter;
        return this;
    }

    private Supplier<F> getter;

    public EntityAttributeDataBuilder<D, E, F> set(Consumer<F> setter) {
        Objects.requireNonNull(setter);
        this.setter = setter;
        return this;
    }

    private Consumer<F> setter;

    public EntityAttribute<E> build() {
        return new EntityAttributeData<D, E>(entityClass) {
            @Override
            protected D getData() {
                return getter.get();
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void setData(D data) {
                Objects.requireNonNull(data);
                setter.accept((F) data);
            }
        };
    }
}
