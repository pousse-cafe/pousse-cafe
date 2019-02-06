package poussecafe.attribute;

public class ReadWriteOptionalAttributeBuilder<T> {

    ReadWriteOptionalAttributeBuilder(CompositeAttribute<T, T> compositeAttribute) {
        this.compositeAttribute = compositeAttribute;
    }

    private CompositeAttribute<T, T> compositeAttribute;

    public OptionalAttribute<T> build() {
        return new OptionalAttribute<T>() {
            @Override
            public T getNullable() {
                return compositeAttribute.getter.get();
            }

            @Override
            public void setNullable(T nullableValue) {
                compositeAttribute.setter.accept(nullableValue);
            }
        };
    }
}
