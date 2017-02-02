package poussecafe.sample.app;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import poussecafe.sample.workflow.CustomerCreation;
import poussecafe.sample.workflow.Messaging;
import poussecafe.sample.workflow.OrderManagement;
import poussecafe.sample.workflow.ProductManagement;

@Configuration
public class WorkflowsConfiguration {

    @Bean
    public OrderManagement orderManagement() {
        return new OrderManagement();
    }

    @Bean
    public CustomerCreation customerCreation() {
        return new CustomerCreation();
    }

    @Bean
    public ProductManagement productManagement() {
        return new ProductManagement();
    }

    @Bean
    public Messaging messaging() {
        return new Messaging();
    }
}
