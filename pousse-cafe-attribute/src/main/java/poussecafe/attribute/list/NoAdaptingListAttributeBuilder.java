package poussecafe.attribute.list;

import java.util.List;
import poussecafe.attribute.ListAttribute;
import poussecafe.attribute.list.ListAttributeBuilder.Complete;

public class NoAdaptingListAttributeBuilder<T> implements Complete<T> {

    NoAdaptingListAttributeBuilder(List<T> list) {
        this.list = list;
    }

    List<T> list;

    @Override
    public ListAttribute<T> build() {
        return new BaseListAttribute<>(list);
    }
}
