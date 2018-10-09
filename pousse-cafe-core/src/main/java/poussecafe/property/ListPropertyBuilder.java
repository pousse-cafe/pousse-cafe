package poussecafe.property;

public class ListPropertyBuilder<T> {

    ListPropertyBuilder() {

    }

    public ReadOnlyListPropertyBuilder<T> read() {
        return new ReadOnlyListPropertyBuilder<>();
    }

    public <U> AdaptingListPropertyBuilder<U, T> from(Class<U> storedElementType) {
        return new AdaptingListPropertyBuilder<>();
    }
}
