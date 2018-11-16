package poussecafe.storage.internal;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;

/**
 * This implementation is heavily inspired by GSON implementation (https://github.com/google/gson).
 * In particular, the TypeToken class.
 *
 * GSON is licensed under the Apache License, Version 2.0;
 */
public class GenericType<T> {

    private final Type type;

    private Class<T> rawType;

    @SuppressWarnings("unchecked")
    protected GenericType() {
        this.type = getTypeParameter(getClass());
        this.rawType = (Class<T>) getRawType(type);
    }

    private Type getTypeParameter(Class<?> subclass) {
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        return parameterized.getActualTypeArguments()[0];
    }

    private Class<?> getRawType(Type type) {
        if (type instanceof Class<?>) {
            return (Class<?>) type;
        } else if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type rawType = parameterizedType.getRawType();
            return (Class<?>) rawType;
        } else if (type instanceof GenericArrayType) {
            Type componentType = ((GenericArrayType) type).getGenericComponentType();
            return Array.newInstance(getRawType(componentType), 0).getClass();
        } else if (type instanceof TypeVariable) {
            return Object.class;
        } else if (type instanceof WildcardType) {
            return getRawType(((WildcardType) type).getUpperBounds()[0]);
        } else {
            String className = type == null ? "null" : type.getClass().getName();
            throw new IllegalArgumentException("Expected a Class, ParameterizedType, or " + "GenericArrayType, but <"
                    + type + "> is of type " + className);
        }
    }

    /**
     * Returns the raw (non-generic) type for this type.
     */
    public Class<T> getRawType() {
        return rawType;
    }

    /**
     * Gets underlying {@code Type} instance.
     */
    public Type getType() {
        return type;
    }
}
