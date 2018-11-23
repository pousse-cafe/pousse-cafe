package poussecafe.property;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import poussecafe.exception.PousseCafeException;

class AutoAdaptingDataAdapter<U, T> {

    AutoAdaptingDataAdapter(Class<T> propertyTypeClass, Class<U> dataAdapterClass) {
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
        Method getAdapter = getAdapter();
        if(getAdapter.getReturnType() != propertyTypeClass) {
            throw new PousseCafeException("adapt() does not return " + propertyTypeClass.getSimpleName());
        }
    }

    private Method setAdapter() {
        try {
            return dataAdapterClass.getMethod("adapt", propertyTypeClass);
        } catch (Exception e) {
            throw new PousseCafeException("Missing adapt(" + propertyTypeClass.getSimpleName() + ")", e);
        }
    }

    private Method getAdapter() {
        try {
            return dataAdapterClass.getMethod("adapt");
        } catch (Exception e) {
            throw new PousseCafeException("Missing adapt()", e);
        }
    }

    @SuppressWarnings("unchecked")
    public T adaptGet(U u) {
        try {
            return (T) getAdapter().invoke(u);
        } catch (Exception e) {
            throw new PousseCafeException("Unable to adapt data while getting", e);
        }
    }

    @SuppressWarnings("unchecked")
    public U adaptSet(T value) {
        try {
            return (U) setAdapter().invoke(null, value);
        } catch (Exception e) {
            throw new PousseCafeException("Unable to adapt data while setting", e);
        }
    }
}
