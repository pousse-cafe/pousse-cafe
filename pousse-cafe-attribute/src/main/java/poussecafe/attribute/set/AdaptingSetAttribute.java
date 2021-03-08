package poussecafe.attribute.set;

import java.util.Collection;
import poussecafe.attribute.SetAttribute;
import poussecafe.attribute.adapters.AdaptingSet;
import poussecafe.attribute.adapters.DataAdapters;

public abstract class AdaptingSetAttribute<F, T> implements SetAttribute<T> {

    public AdaptingSetAttribute(Collection<F> list) {
        setAttribute = new AdaptingSet.Builder<F, T>()
                .mutableCollection(list)
                .adapter(DataAdapters.adapter(this::convertFrom, this::convertTo))
                .build();
    }

    private AdaptingSet<F, T> setAttribute;

    protected abstract T convertFrom(F from);

    protected abstract F convertTo(T from);

    @Override
    public EditableSet<T> value() {
        return setAttribute;
    }
}
