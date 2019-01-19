package poussecafe.property;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;
import poussecafe.domain.Entity;
import poussecafe.domain.EntityData;

public class EntityPropertyBuilder<D extends EntityData<?>, E extends Entity<?, D>, F extends D> {

    EntityPropertyBuilder() {

    }

    Class<E> entityClass;

    Class<F> dataClass;

    public EntityPropertyBuilder<D, E, F> get(Supplier<F> getter) {
        Objects.requireNonNull(getter);
        this.getter = getter;
        return this;
    }

    private Supplier<F> getter;

    public EntityPropertyBuilder<D, E, F> set(Consumer<F> setter) {
        Objects.requireNonNull(setter);
        this.setter = setter;
        return this;
    }

    private Consumer<F> setter;

    public EntityProperty<E> build() {
        return new EntityPropertyData<D, E>(entityClass) {
            @Override
            protected D getData() {
                return getter.get();
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void setData(D data) {
                setter.accept((F) data);
            }
        };
    }
}
