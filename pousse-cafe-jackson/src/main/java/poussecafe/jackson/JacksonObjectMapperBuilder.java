package poussecafe.jackson;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonFormat.Value;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.json.JsonWriteFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.math.BigDecimal;

public class JacksonObjectMapperBuilder {

    public JacksonObjectMapperBuilder() {
        mapper = new ObjectMapper();

        mapFieldsOnly();
        handleStringDates();
        writeNumbersAsStrings();

        mapper.setConfig(mapper.getSerializationConfig()
                .without(SerializationFeature.FAIL_ON_EMPTY_BEANS));
    }

    private ObjectMapper mapper;

    private void handleStringDates() {
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
    }

    private void mapFieldsOnly() {
        mapper.setVisibility(mapper.getSerializationConfig().getDefaultVisibilityChecker()
                .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
                .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withCreatorVisibility(JsonAutoDetect.Visibility.NONE));
    }

    private void writeNumbersAsStrings() {
        mapper.setConfig(mapper.getSerializationConfig()
                .with(JsonWriteFeature.WRITE_NUMBERS_AS_STRINGS));
        mapper.configure(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN, true);
        mapper.configOverride(BigDecimal.class).setFormat(Value.forShape(Shape.STRING));
    }

    public ObjectMapper build() {
        return mapper;
    }

    public JacksonObjectMapperBuilder failOnUnknownProperties(boolean fail) {
        if(!fail) {
            mapper.setConfig(mapper.getDeserializationConfig()
                    .without(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES));
        }
        return this;
    }

    public JacksonObjectMapperBuilder withDefaultTyping(DefaultTyping defaultTyping) {
        mapper.activateDefaultTyping(DEFAULT_VALIDATOR, defaultTyping, As.PROPERTY);
        return this;
    }

    @SuppressWarnings("serial")
    private static final PolymorphicTypeValidator DEFAULT_VALIDATOR = new PolymorphicTypeValidator.Base() {
        @Override
        public Validity validateBaseType(MapperConfig<?> config, JavaType baseType) {
            return Validity.ALLOWED;
        }

        @Override
        public Validity validateSubClassName(MapperConfig<?> config, JavaType baseType, String subClassName)
                throws JsonMappingException {
            return Validity.ALLOWED;
        }

        @Override
        public Validity validateSubType(MapperConfig<?> config, JavaType baseType, JavaType subType)
                throws JsonMappingException {
            return Validity.ALLOWED;
        }
    };
}
