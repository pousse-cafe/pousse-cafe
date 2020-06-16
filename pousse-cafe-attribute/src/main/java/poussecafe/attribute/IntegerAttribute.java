package poussecafe.attribute;

public class IntegerAttribute implements NumberAttribute<Integer> {

    public IntegerAttribute(NumberAttribute<Integer> attribute) {
        this.attribute = attribute;
    }

    private NumberAttribute<Integer> attribute;

    @Override
    public void add(Integer term) {
        attribute.add(term);
    }

    @Override
    public Integer value() {
        return attribute.value();
    }

    @Override
    public void value(Integer value) {
        attribute.value(value);
    }
}
