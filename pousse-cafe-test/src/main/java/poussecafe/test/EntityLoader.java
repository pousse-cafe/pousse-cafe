package poussecafe.test;

import com.fasterxml.jackson.databind.JsonNode;
import poussecafe.domain.EntityAttributes;
import poussecafe.domain.EntityDataAccess;
import poussecafe.environment.EntityImplementation;
import poussecafe.storage.internal.InternalDataAccess;

import static java.util.Objects.requireNonNull;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class EntityLoader {

    public void loadEntity(JsonNode dataJson) {
        EntityAttributes dataImplementation = (EntityAttributes) entityImplementation.getDataFactory().get();
        objectMapper.readJson(dataImplementation, dataJson);
        dataAccess.addData(dataImplementation);
    }

    private EntityImplementation entityImplementation;

    private PousseCafeTestObjectMapper objectMapper = new PousseCafeTestObjectMapper();

    private EntityDataAccess dataAccess;

    public static class Builder {

        private EntityLoader loader = new EntityLoader();

        public EntityLoader build() {
            requireNonNull(loader.entityImplementation);
            requireNonNull(loader.objectMapper);
            requireNonNull(loader.dataAccess);
            return loader;
        }

        public Builder entityImplementation(EntityImplementation entityImplementation) {
            loader.entityImplementation = entityImplementation;
            return this;
        }

        public Builder objectMapper(PousseCafeTestObjectMapper objectMapper) {
            loader.objectMapper = objectMapper;
            return this;
        }

        public Builder dataAccess(InternalDataAccess dataAccess) {
            loader.dataAccess = dataAccess;
            return this;
        }
    }

    private EntityLoader() {

    }
}
