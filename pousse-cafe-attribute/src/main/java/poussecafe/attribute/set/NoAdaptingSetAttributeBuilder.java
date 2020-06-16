package poussecafe.attribute.set;

import java.util.Set;
import poussecafe.attribute.SetAttribute;
import poussecafe.attribute.set.SetAttributeBuilder.Complete;

class NoAdaptingSetAttributeBuilder<T>
implements Complete<T> {

    NoAdaptingSetAttributeBuilder(Set<T> set) {
        this.set = set;
    }

    private Set<T> set;

    @Override
    public SetAttribute<T> build() {
        return new BaseSetAttribute<>(set);
    }
}
