package poussecafe.attribute;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;
import poussecafe.attribute.SingleAttributeBuilder.Complete;
import poussecafe.attribute.SingleAttributeBuilder.ExpectingWriter;

import static java.util.Objects.requireNonNull;

class NoAdaptingSingleAttributeBuilder<T>
implements ExpectingWriter<T>, Complete<T> {

    NoAdaptingSingleAttributeBuilder(Supplier<T> getter) {
        requireNonNull(getter);
        this.getter = getter;
    }

    private Supplier<T> getter;

    @Override
    public Complete<T> write(Consumer<T> setter) {
        requireNonNull(setter);
        this.setter = setter;
        return this;
    }

    private Consumer<T> setter;

    @Override
    public Attribute<T> build() {
        return new Attribute<>() {
            @Override
            public T value() {
                return getter.get();
            }

            @Override
            public void value(T value) {
                Objects.requireNonNull(value);
                setter.accept(value);
            }
        };
    }
}
