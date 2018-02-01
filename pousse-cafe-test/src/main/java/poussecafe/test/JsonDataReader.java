package poussecafe.test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import poussecafe.exception.PousseCafeException;
import poussecafe.storable.IdentifiedStorableData;
import poussecafe.storable.Property;
import poussecafe.util.FieldAccessor;
import poussecafe.util.ReflectionUtils;

public class JsonDataReader {

    public <K, D extends IdentifiedStorableData<K>> void readJson(D dataImplementation,
            JsonNode dataJson) {
        // TODO clean-up this mess
        FieldAccessor accessor = new FieldAccessor(dataImplementation);
        for(Field field : ReflectionUtils.getHierarchyFields(dataImplementation.getClass())) {
            if(field.getGenericType() instanceof ParameterizedType) {
                ParameterizedType fieldType = (ParameterizedType) field.getGenericType();
                if(Property.class.isAssignableFrom((Class<?>) fieldType.getRawType())) {
                    logger.debug("Reading field " + field.getName());
                    Property property = (Property) accessor.get(field.getName());
                    try {
                        JsonNode propertyJson = dataJson.get(field.getName());
                        if(propertyJson != null) {
                            property.set(objectMapper.readValue(propertyJson.traverse(), property.getValueClass()));
                        } else {
                            logger.debug("Property " + field.getName() + " undefined in JSON");
                        }
                    } catch (Exception e) {
                        throw new PousseCafeException("Unable to read json value " + dataJson.toString(), e);
                    }
                } else {
                    logger.debug("Skipping field " + field.getName() + " (not a property)");
                }
            } else {
                logger.debug("Skipping field " + field.getName() + " (not a class)");
            }
        }
    }

    private Logger logger = LoggerFactory.getLogger(getClass());

    private ObjectMapper objectMapper = new ObjectMapper();
}
