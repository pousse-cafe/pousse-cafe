package poussecafe.sample.app;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfiguration {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(mapper
                .getSerializationConfig()
                .getDefaultVisibilityChecker()
                .withFieldVisibility(Visibility.NON_PRIVATE));
        return mapper;
    }
}
