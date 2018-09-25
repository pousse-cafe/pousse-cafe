package poussecafe.storable;

import static poussecafe.check.Checks.checkThatValue;

public class OperatorLessReadWriteNumberPropertyBuilder<T extends Number> {

    OperatorLessReadWriteNumberPropertyBuilder(CompositeProperty<T, T> compositeProperty) {
        this.compositeProperty = compositeProperty;
    }

    private CompositeProperty<T, T> compositeProperty;

    public ReadWriteNumberPropertyBuilder<T> addOperator(AddOperator<T> addOperator) {
        checkThatValue(addOperator).notNull();
        return new ReadWriteNumberPropertyBuilder<>(compositeProperty, addOperator);
    }
}
