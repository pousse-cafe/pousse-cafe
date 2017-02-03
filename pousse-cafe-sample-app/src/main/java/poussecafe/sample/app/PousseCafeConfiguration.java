package poussecafe.sample.app;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import poussecafe.sample.configuration.CustomerConfiguration;
import poussecafe.sample.configuration.MessageConfiguration;
import poussecafe.sample.configuration.OrderConfiguration;
import poussecafe.sample.configuration.ProductConfiguration;
import poussecafe.sample.configuration.SampleAppConfiguration;

@Configuration
public class PousseCafeConfiguration extends SampleAppConfiguration implements InitializingBean {

    @Autowired
    private CustomerConfiguration customerConfiguration;

    @Autowired
    private MessageConfiguration messageConfiguration;

    @Autowired
    private OrderConfiguration orderConfiguration;

    @Autowired
    private ProductConfiguration productConfiguration;

    @Override
    public void afterPropertiesSet()
            throws Exception {
        registerComponents();
    }

    @Override
    protected CustomerConfiguration customerConfiguration() {
        return customerConfiguration;
    }

    @Override
    protected MessageConfiguration messageConfiguration() {
        return messageConfiguration;
    }

    @Override
    protected OrderConfiguration orderConfiguration() {
        return orderConfiguration;
    }

    @Override
    protected ProductConfiguration productConfiguration() {
        return productConfiguration;
    }

}
