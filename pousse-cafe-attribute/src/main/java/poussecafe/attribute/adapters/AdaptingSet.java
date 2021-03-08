package poussecafe.attribute.adapters;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import poussecafe.attribute.set.EditableSet;
import poussecafe.collection.SetEditor;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toSet;

public class AdaptingSet<U, T>
extends AdaptingCollection<U, T>
implements EditableSet<T> {

    @Override
    public SetEditor<T> edit() {
        return new SetEditor<>(this);
    }

    public static class Builder<U, T> {

        public Builder() {
            set.collectionFactory(HashSet::new);
        }

        private AdaptingSet<U, T> set = new AdaptingSet<>();

        public AdaptingSet<U, T> build() {
            requireNonNull(set.mutableCollection());
            requireNonNull(set.adapter());
            return set;
        }

        public Builder<U, T> mutableSet(Set<U> mutableSet) {
            set.mutableCollection(mutableSet);
            return this;
        }

        public Builder<U, T> mutableCollection(Collection<U> mutableSet) {
            set.mutableCollection(mutableSet);
            return this;
        }

        public Builder<U, T> adapter(DataAdapter<U, T> adapter) {
            set.adapter(adapter);
            return this;
        }
    }

    private AdaptingSet() {

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
