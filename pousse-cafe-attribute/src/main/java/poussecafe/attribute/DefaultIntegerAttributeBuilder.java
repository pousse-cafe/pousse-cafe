package poussecafe.attribute;

import java.util.function.Consumer;
import java.util.function.Supplier;
import poussecafe.attribute.IntegerAttributeBuilder.Complete;
import poussecafe.attribute.IntegerAttributeBuilder.ExpectingWriter;

import static java.util.Objects.requireNonNull;

class DefaultIntegerAttributeBuilder implements ExpectingWriter, Complete {

    DefaultIntegerAttributeBuilder(Supplier<Integer> getter) { // NOSONAR
        generic = new GenericNumberAttribute<>();
        generic.addOperator = AddOperators.INTEGER;

        requireNonNull(getter);
        generic.getter = getter;
    }

    private GenericNumberAttribute<Integer> generic;

    @Override
    public Complete write(Consumer<Integer> setter) { // NOSONAR
        requireNonNull(setter);
        generic.setter = setter;
        return this;
    }

    @Override
    public IntegerAttribute build() {
        return new IntegerAttribute(generic);
    }
}
