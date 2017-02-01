package poussecafe.sample.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import poussecafe.configuration.ApplicationContext;
import poussecafe.consequence.CommandProcessor;

@Configuration
@ComponentScan(basePackages = { "poussecafe.sample" })
public class AppConfiguration {

    @Autowired
    private PousseCafeConfiguration pousseCafeConfiguration;

    @Bean
    public ApplicationContext pousseCafeApplicationContext() {
        return new ApplicationContext(pousseCafeConfiguration);
    }

    @Bean
    public CommandProcessor commandProcessor() {
        return pousseCafeApplicationContext().getCommandProcessor();
    }
}
