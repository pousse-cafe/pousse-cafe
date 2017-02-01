package poussecafe.storable;

import poussecafe.storable.SimpleStorable.Data;

import static org.mockito.Mockito.mock;

public class SimpleStorableRepositoryTest
extends ActiveStorableRepositoryTest<SimpleStorableKey, SimpleStorable, SimpleStorable.Data> {

    @Override
    protected ActiveStorableRepository<SimpleStorable, SimpleStorableKey, SimpleStorable.Data> buildRepository() {
        return new SimpleStorableRepository();
    }

    @Override
    protected SimpleStorableKey buildKey() {
        return new SimpleStorableKey();
    }

    @Override
    protected Class<Data> dataClass() {
        return SimpleStorable.Data.class;
    }

    @Override
    protected SimpleStorable mockStorable() {
        return mock(SimpleStorable.class);
    }

}
