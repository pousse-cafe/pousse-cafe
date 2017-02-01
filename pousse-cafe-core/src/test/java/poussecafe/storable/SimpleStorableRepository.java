package poussecafe.storable;

public class SimpleStorableRepository
        extends ActiveStorableRepository<SimpleStorable, SimpleStorableKey, SimpleStorable.Data> {

    @Override
    protected SimpleStorable newStorable() {
        return new SimpleStorable();
    }

}
