package poussecafe.domain;

import java.util.function.Supplier;
import poussecafe.context.Environment;
import poussecafe.exception.PousseCafeException;
import poussecafe.storage.Storage;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public class ComponentFactory {

    public <T> T newComponent(ComponentSpecification<T> specification) {
        Class<T> primitiveClass = specification.getPrimitiveClass();
        T primitive = newComponent(primitiveClass);

        if(Component.class.isAssignableFrom(primitiveClass)) {
            Component realPrimitive = (Component) primitive;
            realPrimitive.setComponentFactory(this);
        }

        if(Entity.class.isAssignableFrom(primitiveClass)) {
            Entity<?, ?> entity = (Entity<?, ?>) primitive;
            if(specification.getExistingData() != null) {
                entity.setData(specification.getExistingData());
            }

            Storage storage = environment.getStorage(primitiveClass);
            if(storage != null) {
                entity.storage(storage);
                entity.messageCollection(storage.getMessageSendingPolicy().newMessageCollection());
            }

            if(environment.hasImplementation(primitiveClass)) {
                Object data = null;
                if(specification.isWithData()) {
                    data = supplyDataImplementation(primitiveClass);
                    entity.setData(data);
                }
            }
        }

        if(Repository.class.isAssignableFrom(primitiveClass)) {
            Repository<?, ?, ?> repository = (Repository<?, ?, ?>) primitive;
            Class<?> entityClass = environment.getEntityClass(primitiveClass);
            repository.setEntityClass(entityClass);
            repository.setDataAccess(supplyDataAccessImplementation(entityClass));
        }

        if(Factory.class.isAssignableFrom(primitiveClass)) {
            Factory<?, ?, ?> factory = (Factory<?, ?, ?>) primitive;
            factory.setEntityClass(environment.getEntityClass(primitiveClass));
        }

        return primitive;
    }

    private <T> T newComponent(Class<T> entityClass) {
        try {
            return entityClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new PousseCafeException("Unable to instantiate entity", e);
        }
    }

    public void setEnvironment(Environment environment) {
        checkThat(value(environment).notNull());
        this.environment = environment;
    }

    private Environment environment;

    private Object supplyDataImplementation(Class<?> entityClass) {
        Supplier<?> factory = environment.getEntityDataFactory(entityClass);
        return factory.get();
    }

    private Object supplyDataAccessImplementation(Class<?> entityClass) {
        Supplier<?> factory = environment.getEntityDataAccessFactory(entityClass);
        return factory.get();
    }
}
