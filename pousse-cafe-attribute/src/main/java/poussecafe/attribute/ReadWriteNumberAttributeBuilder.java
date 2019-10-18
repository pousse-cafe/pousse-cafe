package poussecafe.attribute;

import java.util.Objects;

public class ReadWriteNumberAttributeBuilder<T extends Number> {

    ReadWriteNumberAttributeBuilder(CompositeAttribute<T, T> compositeAttribute,
            AddOperator<T> addOperator) {
        this.compositeAttribute = compositeAttribute;
        this.addOperator = addOperator;
    }

    private CompositeAttribute<T, T> compositeAttribute;

    private AddOperator<T> addOperator;

    public NumberAttribute<T> build() {
        Objects.requireNonNull(addOperator);
        return new NumberAttribute<>() {
            @Override
            public T value() {
                return compositeAttribute.getter.get();
            }

            @Override
            public void value(T value) {
                Objects.requireNonNull(value);
                compositeAttribute.setter.accept(value);
            }

            @Override
            public void add(T term) {
                T result = addOperator.add(value(), term);
                value(result);
            }
        };
    }
}
