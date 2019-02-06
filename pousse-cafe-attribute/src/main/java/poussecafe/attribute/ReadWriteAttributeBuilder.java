package poussecafe.attribute;

import java.util.Objects;

public class ReadWriteAttributeBuilder<T> {

    ReadWriteAttributeBuilder(CompositeAttribute<T, T> compositeAttribute) {
        this.compositeAttribute = compositeAttribute;
    }

    private CompositeAttribute<T, T> compositeAttribute;

    public Attribute<T> build() {
        return new Attribute<T>() {
            @Override
            public T value() {
                return compositeAttribute.getter.get();
            }

            @Override
            public void value(T value) {
                Objects.requireNonNull(value);
                compositeAttribute.setter.accept(value);
            }
        };
    }
}
