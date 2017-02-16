package configuration;

import poussecafe.configuration.MetaApplicationConfiguration;
import workflow.MyWorkflow;

public abstract class SimpleMetaAppConfiguration extends MetaApplicationConfiguration {

    public SimpleMetaAppConfiguration() {
        registerAggregate(myAggregateConfiguration());
        registerWorkflow(new MyWorkflow());
    }

    protected abstract MyAggregateConfiguration myAggregateConfiguration();
}
