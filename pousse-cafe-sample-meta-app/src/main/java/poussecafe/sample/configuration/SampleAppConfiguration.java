package poussecafe.sample.configuration;

import poussecafe.configuration.MetaApplicationConfiguration;
import poussecafe.sample.domain.ContentChooser;
import poussecafe.sample.workflow.CustomerCreation;
import poussecafe.sample.workflow.Messaging;
import poussecafe.sample.workflow.OrderManagement;
import poussecafe.sample.workflow.ProductManagement;

public abstract class SampleAppConfiguration extends MetaApplicationConfiguration {

    protected void registerComponents() {
        registerAggregate(customerConfiguration());
        registerAggregate(messageConfiguration());
        registerAggregate(orderConfiguration());
        registerAggregate(productConfiguration());

        registerService(new ContentChooser());

        registerWorkflow(new CustomerCreation());
        registerWorkflow(new Messaging());
        registerWorkflow(new OrderManagement());
        registerWorkflow(new ProductManagement());
    }

    protected abstract CustomerConfiguration customerConfiguration();

    protected abstract MessageConfiguration messageConfiguration();

    protected abstract OrderConfiguration orderConfiguration();

    protected abstract ProductConfiguration productConfiguration();
}
