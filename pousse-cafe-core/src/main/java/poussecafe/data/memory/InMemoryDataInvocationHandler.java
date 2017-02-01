package poussecafe.data.memory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class InMemoryDataInvocationHandler implements InvocationHandler {

    private InMemoryDataImplementation dataImplementation;

    private PropertyMethodsNamingPolicy methodsNamingPolicy;

    public InMemoryDataInvocationHandler(Class<?> dataType) {
        dataImplementation = new InMemoryDataImplementation(dataType);
        methodsNamingPolicy = new PropertyMethodsNamingPolicy();
    }

    @Override
    public Object invoke(Object proxy,
            Method method,
            Object[] args)
                    throws Throwable {
        if ("equals".equals(method.getName())) {
            return dataImplementation.equals(args[0]);
        } else if ("hashCode".equals(method.getName())) {
            return dataImplementation.hashCode();
        } else if ("toString".equals(method.getName())) {
            return dataImplementation.toString();
        } else {
            return getSetOrIgnore(method, args);
        }
    }

    private Object getSetOrIgnore(Method method,
            Object[] args) {
        if (methodsNamingPolicy.isGetter(method.getName())) {
            return dataImplementation.get(methodsNamingPolicy.extractPropertyName(method.getName()));
        }
        if (methodsNamingPolicy.isSetter(method.getName())) {
            dataImplementation.set(methodsNamingPolicy.extractPropertyName(method.getName()), args[0]);
        }
        return null;
    }

    public InMemoryDataImplementation getDataImplementation() {
        return dataImplementation;
    }

}
