package poussecafe.attribute.list;

import java.util.List;
import poussecafe.attribute.ListAttribute;
import poussecafe.attribute.adapters.AdaptingList;
import poussecafe.attribute.adapters.DataAdapters;

abstract class AdaptingListAttribute<F, T> implements ListAttribute<T> {

    public AdaptingListAttribute(List<F> list) {
        this.list = new AdaptingList.Builder<F, T>()
                .adapter(DataAdapters.adapter(this::convertFrom, this::convertTo))
                .mutableList(list)
                .build();
    }

    private AdaptingList<F, T> list;

    @Override
    public EditableList<T> value() {
        return list;
    }

    protected abstract T convertFrom(F from);

    protected abstract F convertTo(T from);
}
