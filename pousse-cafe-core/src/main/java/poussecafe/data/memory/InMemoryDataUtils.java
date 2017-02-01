
package poussecafe.data.memory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public interface InMemoryDataUtils {

    public static <D> D newDataImplementation(Class<D> dataClass) {
        return new InMemoryDataImplementationFactory<>(dataClass).newImplementation();
    }

    public static InMemoryDataImplementation getDataImplementation(Object proxy) {
        if (proxy instanceof Proxy) {
            InvocationHandler invocationHandler = Proxy.getInvocationHandler(proxy);
            if (invocationHandler instanceof InMemoryDataInvocationHandler) {
                InMemoryDataInvocationHandler inMemoryDataInvocationHandler = (InMemoryDataInvocationHandler) invocationHandler;
                return inMemoryDataInvocationHandler.getDataImplementation();
            } else {
                throw new InMemoryDataException("Given proxy is not proxying an in-memory data implementation");
            }
        } else {
            throw new InMemoryDataException("Given object is not a proxy");
        }
    }

}
