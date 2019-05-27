package poussecafe.attribute.entity;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;
import poussecafe.domain.Entity;
import poussecafe.domain.EntityAttributes;

public class SingleEntityAttributeBuilder<D extends EntityAttributes<?>, E extends Entity<?, D>, F extends D> {

    SingleEntityAttributeBuilder() {

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
            SingleEntityAttributeBuilder.this.setter = setter;
            return new ReadWriteAttribute();
        }
    }

    private Consumer<F> setter;

    public class ReadWriteAttribute {

        private ReadWriteAttribute() {

        }

        public EntityAttribute<E> build() {
            return new EntityAttributeData<>(entityClass) {
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


}
