package poussecafe.property;

import java.util.List;

public class ReadWriteListPropertyBuilder<T> {

    ReadWriteListPropertyBuilder(List<T> list) {
        this.list = list;
    }

    List<T> list;

    public ListProperty<T> build() {
        return new BaseListProperty<>(list);
    }
}
