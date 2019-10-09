package poussecafe.messaging;

import java.io.Closeable;
import java.util.function.Supplier;

public interface EnvelopeSource<E> extends Supplier<E>, Closeable {

}
