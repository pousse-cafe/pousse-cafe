package poussecafe.attribute;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

class CompositeAttribute<G, S> {

    Supplier<G> getter;

    Consumer<S> setter;

    <M> CompositeAttribute<M, S> mapGetter(Function<G, M> mapper) {
        CompositeAttribute<M, S> newAttribute = new CompositeAttribute<>();
        newAttribute.getter = () -> mapper.apply(getter.get());
        newAttribute.setter = setter;
        return newAttribute;
    }

    <M> CompositeAttribute<G, M> mapSetter(Function<M, S> mapper) {
        CompositeAttribute<G, M> newAttribute = new CompositeAttribute<>();
        newAttribute.getter = getter;
        newAttribute.setter = value -> setter.accept(mapper.apply(value));
        return newAttribute;
    }
}
