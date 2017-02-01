package poussecafe.sample.app;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WorkflowsConfiguration {

    @Bean
    public PlacingOrder placingOrder() {
        return new PlacingOrder();
    }
}
