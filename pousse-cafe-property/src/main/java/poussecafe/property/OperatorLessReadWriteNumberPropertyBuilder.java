package poussecafe.property;

import java.util.Objects;

public class OperatorLessReadWriteNumberPropertyBuilder<T extends Number> {

    OperatorLessReadWriteNumberPropertyBuilder(CompositeProperty<T, T> compositeProperty) {
        this.compositeProperty = compositeProperty;
    }

    private CompositeProperty<T, T> compositeProperty;

    public ReadWriteNumberPropertyBuilder<T> addOperator(AddOperator<T> addOperator) {
        Objects.requireNonNull(addOperator);
        return new ReadWriteNumberPropertyBuilder<>(compositeProperty, addOperator);
    }
}
