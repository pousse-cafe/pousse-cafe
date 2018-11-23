package poussecafe.property.adapters;

public interface DataAdapter<U, T> {

    T adaptGet(U storedValue);

    U adaptSet(T valueToStore);
}
