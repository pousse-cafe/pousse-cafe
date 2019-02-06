package poussecafe.attribute;

import java.util.Objects;

public class ReadWriteIntegerAttributeBuilder {

    public ReadWriteIntegerAttributeBuilder(CompositeAttribute<Integer, Integer> compositeAttribute) {
        this.compositeAttribute = compositeAttribute;
    }

    private CompositeAttribute<Integer, Integer> compositeAttribute;

    public IntegerAttribute build() {
        return new IntegerAttribute() {
            @Override
            public Integer value() {
                return compositeAttribute.getter.get();
            }

            @Override
            public void value(Integer value) {
                Objects.requireNonNull(value);
                compositeAttribute.setter.accept(value);
            }
        };
    }
}
