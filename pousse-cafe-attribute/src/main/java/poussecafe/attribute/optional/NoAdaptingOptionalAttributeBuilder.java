package poussecafe.attribute.optional;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;
import poussecafe.attribute.OptionalAttribute;
import poussecafe.attribute.optional.OptionalAttributeBuilder.Complete;
import poussecafe.attribute.optional.OptionalAttributeBuilder.ExpecingWriter;

class NoAdaptingOptionalAttributeBuilder<T>
implements ExpecingWriter<T>, Complete<T> {

    NoAdaptingOptionalAttributeBuilder(Supplier<T> getter) {
        Objects.requireNonNull(getter);
        this.getter = getter;
    }

    private Supplier<T> getter;

    @Override
    public Complete<T> write(Consumer<T> setter) {
        this.setter = setter;
        return this;
    }

    private Consumer<T> setter;

    @Override
    public OptionalAttribute<T> build() {
        return new BaseOptionalAttribute<>() {
            @Override
            public T nullableValue() {
                T storedValue = getter.get();
                if(storedValue == null) {
                    return null;
                } else {
                    return storedValue;
                }
            }

            @Override
            public void optionalValue(T nullableValue) {
                if(nullableValue == null) {
                    setter.accept(null);
                } else {
                    setter.accept(nullableValue);
                }
            }
        };
    }
}
