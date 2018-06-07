package poussecafe.storable;

public abstract class IntegerProperty implements Property<Integer> {

    public void add(int term) {
        set(get() + term);
    }
}
