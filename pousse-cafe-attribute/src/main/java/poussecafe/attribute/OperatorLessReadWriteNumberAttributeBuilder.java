package poussecafe.attribute;

import java.util.Objects;

public class OperatorLessReadWriteNumberAttributeBuilder<T extends Number> {

    OperatorLessReadWriteNumberAttributeBuilder(CompositeAttribute<T, T> compositeAttribute) {
        this.compositeAttribute = compositeAttribute;
    }

    private CompositeAttribute<T, T> compositeAttribute;

    public ReadWriteNumberAttributeBuilder<T> addOperator(AddOperator<T> addOperator) {
        Objects.requireNonNull(addOperator);
        return new ReadWriteNumberAttributeBuilder<>(compositeAttribute, addOperator);
    }
}
