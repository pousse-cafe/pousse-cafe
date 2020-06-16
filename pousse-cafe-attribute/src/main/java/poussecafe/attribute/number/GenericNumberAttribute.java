package poussecafe.attribute.number;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;
import poussecafe.attribute.AddOperator;
import poussecafe.attribute.NumberAttribute;

class GenericNumberAttribute<T extends Number> implements NumberAttribute<T> {

    Supplier<T> getter;

    Consumer<T> setter;

    AddOperator<T> addOperator;

    @Override
    public T value() {
        return getter.get();
    }

    @Override
    public void value(T value) {
        Objects.requireNonNull(value);
        setter.accept(value);
    }

    @Override
    public void add(T term) {
        T result = addOperator.add(value(), term);
        value(result);
    }
}
