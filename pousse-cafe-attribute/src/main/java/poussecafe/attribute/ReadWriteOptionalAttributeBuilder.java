package poussecafe.attribute;

public class ReadWriteOptionalAttributeBuilder<T> {

    ReadWriteOptionalAttributeBuilder(CompositeAttribute<T, T> compositeAttribute) {
        this.compositeAttribute = compositeAttribute;
    }

    private CompositeAttribute<T, T> compositeAttribute;

    public OptionalAttribute<T> build() {
        return new OptionalAttribute<T>() {
            @Override
            public T nullableValue() {
                return compositeAttribute.getter.get();
            }

            @Override
            public void optionalValue(T nullableValue) {
                compositeAttribute.setter.accept(nullableValue);
            }
        };
    }
}
