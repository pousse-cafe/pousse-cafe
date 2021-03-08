package poussecafe.attribute;

/**
 * <p>Auto-adapters are used to combine in a single place the data representation of the adapted type and conversion
 * logic.</p>
 *
 * <p>An auto-adapter has at least 2 methods:</p>
 *
 * <ul>
 * <li>a static method <code>adapt(T)</code> returning an instantiate of the auto-adapter given an instance of the adapted type,</li>
 * <li>an instance method <code>adapt()</code> returning an instance of the adapted type.</li>
 * </ul>
 *
 * @param <T> the adapted type
 */
public interface AutoAdapter<T> {

    T adapt();
}
