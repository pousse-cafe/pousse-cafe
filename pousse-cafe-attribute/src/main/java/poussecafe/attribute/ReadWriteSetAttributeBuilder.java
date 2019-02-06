package poussecafe.attribute;

import java.util.Set;

public class ReadWriteSetAttributeBuilder<T> {

    ReadWriteSetAttributeBuilder(Set<T> set) {
        this.set = set;
    }

    private Set<T> set;

    public SetAttribute<T> build() {
        return new BaseSetAttribute<>(set);
    }
}
