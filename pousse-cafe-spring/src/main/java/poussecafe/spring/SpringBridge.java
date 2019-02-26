package poussecafe.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Configuration;
import poussecafe.context.AggregateServices;
import poussecafe.context.MessageSenderLocator;
import poussecafe.context.MetaApplicationContext;
import poussecafe.domain.Repository;
import poussecafe.environment.MessageFactory;
import poussecafe.process.DomainProcess;

@Configuration
public class SpringBridge implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
        pousseCafeContext = beanFactory.getBean(MetaApplicationContext.class);
        registerCoreComponents();
        registerEntityServices();
        registerDomainProcesses();
        registerServices();
    }

    private void registerCoreComponents() {
        MessageSenderLocator messageSenderLocator = pousseCafeContext.getMessageSenderLocator();
        registerInstance(beanName(messageSenderLocator), messageSenderLocator);

        MessageFactory componentFactory = pousseCafeContext.environment().messageFactory();
        registerInstance(beanName(componentFactory), componentFactory);
    }

    private ConfigurableListableBeanFactory beanFactory;

    private MetaApplicationContext pousseCafeContext;

    private void registerEntityServices() {
        for(AggregateServices services : pousseCafeContext.environment().aggregateServices()) {
            registeringFactory(services);
            registeringRepository(services);
        }
    }

    private void registeringFactory(AggregateServices services) {
        String beanName = beanName(services.getFactory());
        logger.debug("Registering factory {}", services.getFactory().getClass().getSimpleName());
        registerInstance(beanName, services.getFactory());
    }

    private String beanName(Object instance) {
        return BEAN_NAME_PREFIX + instance.getClass().getName();
    }

    private static final String BEAN_NAME_PREFIX = "pousseCafe";

    private void registerInstance(String beanName, Object instance) {
        beanFactory.registerSingleton(beanName, instance);
    }

    private void registeringRepository(AggregateServices services) {
        Repository<?, ?, ?> repository = services.getRepository();
        String beanName = beanName(repository);
        logger.debug("Registering repository {}", repository.getClass().getSimpleName());
        registerInstance(beanName, repository);
    }

    private void registerDomainProcesses() {
        for(DomainProcess process : pousseCafeContext.environment().domainProcesses()) {
            String beanName = beanName(process);
            logger.debug("Registering domain process {}", process.getClass().getSimpleName());
            registerInstance(beanName, process);
        }
    }

    private void registerServices() {
        for(Object service : pousseCafeContext.environment().services()) {
            String beanName = beanName(service);
            logger.debug("Registering service {}", service.getClass().getSimpleName());
            registerInstance(beanName, service);
        }
    }

    private Logger logger = LoggerFactory.getLogger(getClass());
}
