package poussecafe.attribute;

import java.util.List;

public class ReadWriteListAttributeBuilder<T> {

    ReadWriteListAttributeBuilder(List<T> list) {
        this.list = list;
    }

    List<T> list;

    public ListAttribute<T> build() {
        return new BaseListAttribute<>(list);
    }
}
