package poussecafe.storage.memory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import poussecafe.storable.BaseProperty;

public class InlineProperty<T extends Serializable> extends BaseProperty<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    public static <T extends Serializable> InlineProperty<T> withValue(T value) {
        @SuppressWarnings("unchecked")
        InlineProperty<T> property = new InlineProperty<>((Class<T>) value.getClass());
        property.set(value);
        return property;
    }

    private T value;

    public InlineProperty(Class<? extends T> valueClass) {
        super(valueClass);
    }

    @SuppressWarnings("unused")
    private InlineProperty() {
        // For deserialization
    }

    @Override
    protected T getValue() {
        return value;
    }

    @Override
    protected void setValue(T value) {
        this.value = value;
    }

    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.writeObject(valueClass);
        stream.writeObject(value);
    }

    @SuppressWarnings("unchecked")
    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        valueClass = (Class<T>) stream.readObject();
        value = (T) stream.readObject();
    }
}
