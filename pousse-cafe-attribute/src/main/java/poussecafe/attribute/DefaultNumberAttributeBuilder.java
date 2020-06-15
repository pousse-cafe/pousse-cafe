package poussecafe.attribute;

import java.util.function.Consumer;
import java.util.function.Supplier;
import poussecafe.attribute.NumberAttributeBuilder.Complete;
import poussecafe.attribute.NumberAttributeBuilder.ExpectingAddOperator;
import poussecafe.attribute.NumberAttributeBuilder.ExpectingWriter;

import static java.util.Objects.requireNonNull;

public class DefaultNumberAttributeBuilder<T extends Number>
implements ExpectingWriter<T>, ExpectingAddOperator<T>, Complete<T> {

    DefaultNumberAttributeBuilder(Supplier<T> getter) {
        requireNonNull(getter);
        attribute.getter = getter;
    }

    private GenericNumberAttribute<T> attribute = new GenericNumberAttribute<>();

    @Override
    public ExpectingAddOperator<T> write(Consumer<T> setter) {
        requireNonNull(setter);
        attribute.setter = setter;
        return this;
    }

    @Override
    public Complete<T> addOperator(AddOperator<T> addOperator) {
        requireNonNull(addOperator);
        attribute.addOperator = addOperator;
        return this;
    }

    @Override
    public NumberAttribute<T> build() {
        return attribute;
    }
}
