package poussecafe.storable;

import java.util.Set;

public class ReadWriteSetPropertyBuilder<T> {

    ReadWriteSetPropertyBuilder() {

    }

    public SetProperty<T> build(Set<T> list) {
        return new BaseSetProperty<>(list);
    }
}
