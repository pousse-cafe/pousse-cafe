package poussecafe.property;

import java.util.List;

public class ReadWriteListPropertyBuilder<T> {

    ReadWriteListPropertyBuilder() {

    }

    public ListProperty<T> build(List<T> list) {
        return new BaseListProperty<>(list);
    }
}
