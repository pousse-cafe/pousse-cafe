package poussecafe.storable;

public class ReadWriteOptionalPropertyBuilder<T> {

    ReadWriteOptionalPropertyBuilder(CompositeProperty<T, T> compositeProperty) {
        this.compositeProperty = compositeProperty;
    }

    private CompositeProperty<T, T> compositeProperty;

    public OptionalProperty<T> build() {
        return new OptionalProperty<T>() {
            @Override
            public T getNullable() {
                return compositeProperty.getter.get();
            }

            @Override
            public void setNullable(T nullableValue) {
                compositeProperty.setter.accept(nullableValue);
            }
        };
    }
}
