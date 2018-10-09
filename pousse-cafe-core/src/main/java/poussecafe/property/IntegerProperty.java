package poussecafe.property;

public abstract class IntegerProperty implements NumberProperty<Integer> {

    @Override
    public void add(Integer term) {
        set(get() + term);
    }
}
