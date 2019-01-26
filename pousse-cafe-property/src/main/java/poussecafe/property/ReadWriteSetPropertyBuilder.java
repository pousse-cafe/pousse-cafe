package poussecafe.property;

import java.util.Set;

public class ReadWriteSetPropertyBuilder<T> {

    ReadWriteSetPropertyBuilder(Set<T> set) {
        this.set = set;
    }

    private Set<T> set;

    public SetProperty<T> build() {
        return new BaseSetProperty<>(set);
    }
}
