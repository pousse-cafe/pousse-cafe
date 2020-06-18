package poussecafe.attribute.list;

import java.util.List;
import poussecafe.attribute.ListAttribute;
import poussecafe.attribute.adapters.AdaptingMutableList;
import poussecafe.attribute.adapters.DataAdapters;

abstract class AdaptingListAttribute<F, T> implements ListAttribute<T> {

    public AdaptingListAttribute(List<F> list) {
        listAttribute = new AdaptingMutableList.Builder<F, T>()
                .adapter(DataAdapters.adapter(this::convertFrom, this::convertTo))
                .mutableList(list)
                .build();
    }

    private AdaptingMutableList<F, T> listAttribute;

    @Override
    public EditableList<T> mutableValue() {
        return listAttribute;
    }

    protected abstract T convertFrom(F from);

    protected abstract F convertTo(T from);
}
