package poussecafe.storable;

import poussecafe.data.memory.InMemoryDataFactory;

public class SimpleStorableFactoryTest extends
ActiveStorableFactoryTest<SimpleStorableKey, SimpleStorable.Data, SimpleStorable, SimpleStorableFactory> {

    @Override
    protected SimpleStorableKey buildKey() {
        return new SimpleStorableKey();
    }

    @Override
    protected SimpleStorableFactory factory() {
        return new SimpleStorableFactory(new InMemoryDataFactory<>(SimpleStorable.Data.class));
    }

}
