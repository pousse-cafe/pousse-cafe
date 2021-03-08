package poussecafe.attribute.adapters;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import poussecafe.attribute.AutoAdapter;
import poussecafe.exception.PousseCafeException;

public class AutoAdaptingDataAdapter<U extends AutoAdapter<T>, T> implements DataAdapter<U, T> {

    public AutoAdaptingDataAdapter(Class<T> propertyTypeClass, Class<U> dataAdapterClass) {
        this.propertyTypeClass = propertyTypeClass;
        this.dataAdapterClass = dataAdapterClass;
        requiresDataAdapter();
    }

    private Class<T> propertyTypeClass;

    private Class<U> dataAdapterClass;

    private void requiresDataAdapter() {
        Method setAdapter = setAdapter();
        if(!Modifier.isStatic(setAdapter.getModifiers())) {
            throw new PousseCafeException("adapt(" + propertyTypeClass.getSimpleName() + ") is not static");
        }
    }

    private Method setAdapter() {
        try {
            return dataAdapterClass.getMethod("adapt", propertyTypeClass);
        } catch (Exception e) {
            throw new PousseCafeException("Missing adapt(" + propertyTypeClass.getSimpleName() + ")", e);
        }
    }

    @Override
    public T adaptGet(U u) {
        try {
            return u.adapt();
        } catch (Exception e) {
            throw new PousseCafeException("Unable to adapt data while getting", e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public U adaptSet(T value) {
        try {
            return (U) setAdapter().invoke(null, value);
        } catch (Exception e) {
            throw new PousseCafeException("Unable to adapt data while setting", e);
        }
    }
}
