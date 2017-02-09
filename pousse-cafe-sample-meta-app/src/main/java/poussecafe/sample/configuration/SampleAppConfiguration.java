package poussecafe.sample.configuration;

import poussecafe.configuration.MetaApplicationConfiguration;
import poussecafe.sample.domain.ContentChooser;
import poussecafe.sample.workflow.CustomerCreation;
import poussecafe.sample.workflow.Messaging;
import poussecafe.sample.workflow.OrderPlacement;
import poussecafe.sample.workflow.OrderSettlement;
import poussecafe.sample.workflow.OrderShipment;
import poussecafe.sample.workflow.ProductManagement;

public abstract class SampleAppConfiguration extends MetaApplicationConfiguration {

    public void registerComponents() {
        registerAggregate(customerConfiguration());
        registerAggregate(messageConfiguration());
        registerAggregate(orderConfiguration());
        registerAggregate(productConfiguration());

        registerService(new ContentChooser());

        registerWorkflow(new CustomerCreation());
        registerWorkflow(new Messaging());
        registerWorkflow(new OrderPlacement());
        registerWorkflow(new OrderSettlement());
        registerWorkflow(new OrderShipment());
        registerWorkflow(new ProductManagement());
    }

    protected abstract CustomerConfiguration customerConfiguration();

    protected abstract MessageConfiguration messageConfiguration();

    protected abstract OrderConfiguration orderConfiguration();

    protected abstract ProductConfiguration productConfiguration();
}
