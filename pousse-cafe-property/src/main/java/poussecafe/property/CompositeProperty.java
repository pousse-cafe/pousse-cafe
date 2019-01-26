package poussecafe.property;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

class CompositeProperty<G, S> {

    Supplier<G> getter;

    Consumer<S> setter;

    <M> CompositeProperty<M, S> mapGetter(Function<G, M> mapper) {
        CompositeProperty<M, S> newProperty = new CompositeProperty<>();
        newProperty.getter = () -> mapper.apply(getter.get());
        newProperty.setter = setter;
        return newProperty;
    }

    <M> CompositeProperty<G, M> mapSetter(Function<M, S> mapper) {
        CompositeProperty<G, M> newProperty = new CompositeProperty<>();
        newProperty.getter = getter;
        newProperty.setter = value -> setter.accept(mapper.apply(value));
        return newProperty;
    }
}
