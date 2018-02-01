package poussecafe.storable;

import java.util.function.Supplier;
import poussecafe.exception.PousseCafeException;
import poussecafe.storage.Storage;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public class PrimitiveFactory {

    public <T> T newPrimitive(PrimitiveSpecification<T> specification) {
        Class<T> primitiveClass = specification.getPrimitiveClass();
        T primitive = newPrimitive(primitiveClass);
        if(Primitive.class.isAssignableFrom(primitiveClass)) {
            Primitive realPrimitive = (Primitive) primitive;
            realPrimitive.setPrimitiveFactory(this);
        }
        if(Storable.class.isAssignableFrom(primitiveClass)) {
            Storable<?> storable = (Storable<?>) primitive;

            if(specification.isWithData()) {
                storable.setData(supplyDataImplementation(primitiveClass));
            }
            if(ActiveStorable.class.isAssignableFrom(primitiveClass)) {
                ActiveStorable<?, ?> activeStorable = (ActiveStorable<?, ?>) primitive;
                Storage storage = environment.getStorage(primitiveClass);
                activeStorable.setStorage(storage);
            }
        }
        if(IdentifiedStorableRepository.class.isAssignableFrom(primitiveClass)) {
            IdentifiedStorableRepository<?, ?, ?> repository = (IdentifiedStorableRepository<?, ?, ?>) primitive;
            Class<?> storableClass = environment.getStorableClass(primitiveClass);
            repository.setStorableClass(storableClass);
            repository.setDataAccess(supplyDataAccessImplementation(storableClass));
        }
        if(IdentifiedStorableFactory.class.isAssignableFrom(primitiveClass)) {
            IdentifiedStorableFactory<?, ?, ?> factory = (IdentifiedStorableFactory<?, ?, ?>) primitive;
            factory.setStorableClass(environment.getStorableClass(primitiveClass));
        }
        return primitive;
    }

    private <T> T newPrimitive(Class<T> storableClass) {
        try {
            return storableClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new PousseCafeException("Unable to instantiate storable", e);
        }
    }

    public void setEnvironment(Environment environment) {
        checkThat(value(environment).notNull());
        this.environment = environment;
    }

    private Environment environment;

    private Object supplyDataImplementation(Class<?> storableClass) {
        Supplier<?> factory = environment.getStorableDataFactory(storableClass);
        return factory.get();
    }

    private Object supplyDataAccessImplementation(Class<?> storableClass) {
        Supplier<?> factory = environment.getStorableDataAccessFactory(storableClass);
        return factory.get();
    }
}
