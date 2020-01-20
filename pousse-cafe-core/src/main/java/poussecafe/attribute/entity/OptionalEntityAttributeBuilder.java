package poussecafe.attribute.entity;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;
import poussecafe.domain.Entity;
import poussecafe.domain.EntityAttributes;

public class OptionalEntityAttributeBuilder<D extends EntityAttributes<?>, E extends Entity<?, D>, F extends D> {

    OptionalEntityAttributeBuilder() {

    }

    Class<E> entityClass;

    Class<F> dataClass;

    public ReadOnlyAttribute read(Supplier<F> getter) {
        Objects.requireNonNull(getter);
        this.getter = getter;
        return new ReadOnlyAttribute();
    }

    private Supplier<F> getter;

    public class ReadOnlyAttribute {

        private ReadOnlyAttribute() {

        }

        public ReadWriteAttribute write(Consumer<F> setter) {
            Objects.requireNonNull(setter);
            OptionalEntityAttributeBuilder.this.setter = setter;
            return new ReadWriteAttribute();
        }
    }

    private Consumer<F> setter;

    public class ReadWriteAttribute {

        private ReadWriteAttribute() {

        }

        public OptionalEntityAttribute<E> build() {
            return new OptionalEntityAttributeData<>(entityClass) {
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
}
