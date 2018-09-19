package poussecafe.storable;

import java.util.List;
import poussecafe.storage.memory.BaseListProperty;

public class ReadWriteListPropertyBuilder<T> {

    ReadWriteListPropertyBuilder() {

    }

    public ListProperty<T> build(List<T> list) {
        return new BaseListProperty<>(list);
    }
}
