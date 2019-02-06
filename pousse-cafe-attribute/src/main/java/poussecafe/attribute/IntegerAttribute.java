package poussecafe.attribute;

public abstract class IntegerAttribute implements NumberAttribute<Integer> {

    @Override
    public void add(Integer term) {
        value(value() + term);
    }
}
