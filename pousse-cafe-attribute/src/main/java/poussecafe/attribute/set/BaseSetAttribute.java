package poussecafe.attribute.set;

import java.util.Objects;
import java.util.Set;
import poussecafe.attribute.SetAttribute;

public class BaseSetAttribute<T> implements SetAttribute<T> {

    public BaseSetAttribute(Set<T> set) {
        Objects.requireNonNull(set);
        this.set = set;
    }

    private Set<T> set;

    @Override
    public EditableSet<T> value() {
        return new SimpleEditableSet<>(set);
    }
}
