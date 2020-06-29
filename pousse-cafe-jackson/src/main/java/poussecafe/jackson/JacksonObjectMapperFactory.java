package poussecafe.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import poussecafe.domain.Service;

public class JacksonObjectMapperFactory implements Service {

    public ObjectMapper buildMapper() {
        return staticBuildMapper();
    }

    public static ObjectMapper staticBuildMapper() {
        return new JacksonObjectMapperBuilder()
                .failOnUnknownProperties(false)
                .withDefaultTyping(DefaultTyping.NON_FINAL)
                .build();
    }

    public static ObjectMapper staticBuildMapper(DefaultTyping defaultTyping) {
        return new JacksonObjectMapperBuilder()
                .failOnUnknownProperties(false)
                .withDefaultTyping(defaultTyping)
                .build();
    }
}
