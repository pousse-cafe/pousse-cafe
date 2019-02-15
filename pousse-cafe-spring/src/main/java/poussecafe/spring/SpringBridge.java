package poussecafe.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Configuration;
import poussecafe.context.EntityServices;
import poussecafe.context.MessageSenderLocator;
import poussecafe.context.MetaApplicationContext;
import poussecafe.domain.MessageFactory;
import poussecafe.domain.Repository;
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

        MessageFactory componentFactory = pousseCafeContext.getMessageFactory();
        registerInstance(beanName(componentFactory), componentFactory);
    }

    private ConfigurableListableBeanFactory beanFactory;

    private MetaApplicationContext pousseCafeContext;

    private void registerEntityServices() {
        for(EntityServices services : pousseCafeContext.getAllEntityServices()) {
            registeringFactory(services);
            registeringRepository(services);
        }
    }

    private void registeringFactory(EntityServices services) {
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

    private void registeringRepository(EntityServices services) {
        Repository<?, ?, ?> repository = services.getRepository();
        String beanName = beanName(repository);
        logger.debug("Registering repository {}", repository.getClass().getSimpleName());
        registerInstance(beanName, repository);
    }

    private void registerDomainProcesses() {
        for(DomainProcess process : pousseCafeContext.getAllDomainProcesses()) {
            String beanName = beanName(process);
            logger.debug("Registering domain process {}", process.getClass().getSimpleName());
            registerInstance(beanName, process);
        }
    }

    private void registerServices() {
        for(Object service : pousseCafeContext.getAllServices()) {
            String beanName = beanName(service);
            logger.debug("Registering service {}", service.getClass().getSimpleName());
            registerInstance(beanName, service);
        }
    }

    private Logger logger = LoggerFactory.getLogger(getClass());
}
