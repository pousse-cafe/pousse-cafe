package poussecafe.attribute.list;

import java.util.List;
import java.util.Objects;
import poussecafe.attribute.ListAttribute;

public class BaseListAttribute<T> implements ListAttribute<T> {

    public BaseListAttribute(List<T> list) {
        Objects.requireNonNull(list);
        this.list = list;
    }

    private List<T> list;

    @Override
    public EditableList<T> mutableValue() {
        return new SimpleEditableList<>(list);
    }
}
