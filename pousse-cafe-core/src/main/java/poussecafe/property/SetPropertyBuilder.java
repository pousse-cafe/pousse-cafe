package poussecafe.property;

public class SetPropertyBuilder<T> {

    SetPropertyBuilder() {

    }

    public ReadOnlySetPropertyBuilder<T> read() {
        return new ReadOnlySetPropertyBuilder<>();
    }

    public <U> AdaptingSetPropertyBuilder<U, T> from(Class<U> storedElementType) {
        return new AdaptingSetPropertyBuilder<>();
    }
}
