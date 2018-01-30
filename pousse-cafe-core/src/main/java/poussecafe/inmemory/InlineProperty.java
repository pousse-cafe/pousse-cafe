package poussecafe.inmemory;

public class InlineProperty<T> extends BaseProperty<T> {

    public static <T> InlineProperty<T> withValue(T value) {
        @SuppressWarnings("unchecked")
        InlineProperty<T> property = new InlineProperty<>((Class<T>) value.getClass());
        property.set(value);
        return property;
    }

    private T property;

    public InlineProperty(Class<T> valueClass) {
        super(valueClass);
    }

    @Override
    protected T getValue() {
        return property;
    }

    @Override
    protected void setValue(T value) {
        property = value;
    }

}
