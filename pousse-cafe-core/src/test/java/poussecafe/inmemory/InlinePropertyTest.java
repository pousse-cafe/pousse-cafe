package poussecafe.inmemory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.junit.Test;
import poussecafe.exception.PousseCafeException;

import static org.junit.Assert.assertTrue;

public class InlinePropertyTest {

    @Test
    public void inlinePropertyIsSerializable() {
        givenInlineProperty();
        whenSerializingAndUnserializing();
        thenUnserializedPropertySameAsGiven();
    }

    private void givenInlineProperty() {
        inline = new InlineProperty<>(Integer.class);
        inline.set(10);
    }

    private InlineProperty<Integer> inline;

    @SuppressWarnings("unchecked")
    private void whenSerializingAndUnserializing() {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(inline);
            oos.close();

            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            unserialized = (InlineProperty<Integer>) new ObjectInputStream(bais).readObject();
        } catch (ClassNotFoundException | IOException e) {
            throw new PousseCafeException(e);
        }
    }

    private InlineProperty<Integer> unserialized;

    private void thenUnserializedPropertySameAsGiven() {
        assertTrue(inline.equals(unserialized));
    }
}
