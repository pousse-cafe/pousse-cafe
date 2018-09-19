package poussecafe.storable;

import static poussecafe.check.Checks.checkThatValue;

public class ReadWriteNumberPropertyBuilder<T extends Number> {

    ReadWriteNumberPropertyBuilder(CompositeProperty<T, T> compositeProperty) {
        this.compositeProperty = compositeProperty;
    }

    private CompositeProperty<T, T> compositeProperty;

    public ReadWriteNumberPropertyBuilder<T> addOperator(AddOperator<T> addOperator) {
        checkThatValue(addOperator).notNull();
        this.addOperator = addOperator;
        return this;
    }

    private AddOperator<T> addOperator;

    public NumberProperty<T> build() {
        checkThatValue(addOperator).notNull();
        return new NumberProperty<T>() {
            @Override
            public T get() {
                return compositeProperty.getter.get();
            }

            @Override
            public void set(T value) {
                compositeProperty.setter.accept(value);
            }

            @Override
            public void add(T term) {
                T result = addOperator.add(get(), term);
                set(result);
            }
        };
    }
}
