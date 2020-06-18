package poussecafe.attribute.adapters;

import java.util.Set;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toSet;

public class AdaptingMutableSet<U, T>
extends AdaptingCollection<U, T>
implements Set<T> {

    public static class Builder<U, T> {

        private AdaptingMutableSet<U, T> set = new AdaptingMutableSet<>();

        public AdaptingMutableSet<U, T> build() {
            requireNonNull(set.mutableCollection());
            requireNonNull(set.adapter());
            return set;
        }

        public Builder<U, T> mutableSet(Set<U> mutableSet) {
            set.mutableCollection(mutableSet);
            return this;
        }

        public Builder<U, T> adapter(DataAdapter<U, T> adapter) {
            set.adapter(adapter);
            return this;
        }
    }

    private AdaptingMutableSet() {

    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Set) {
            return adaptedSet().equals(obj);
        } else {
            return false;
        }
    }

    private Set<T> adaptedSet() {
        return mutableCollection().stream().map(element -> adapter().adaptGet(element)).collect(toSet());
    }

    @Override
    public int hashCode() {
        return adaptedSet().hashCode();
    }

    @Override
    public String toString() {
        return adaptedSet().toString();
    }
}
