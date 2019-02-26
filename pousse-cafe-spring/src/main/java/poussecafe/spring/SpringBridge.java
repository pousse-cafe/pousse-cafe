package poussecafe.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Configuration;
import poussecafe.domain.Repository;
import poussecafe.environment.MessageFactory;
import poussecafe.process.DomainProcess;
import poussecafe.runtime.AggregateServices;
import poussecafe.runtime.MessageSenderLocator;
import poussecafe.runtime.Runtime;

@Configuration
public class SpringBridge implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
        pousseCafeRuntime = beanFactory.getBean(Runtime.class);
        registerCoreComponents();
        registerAggregateServices();
        registerDomainProcesses();
        registerServices();
    }

    private ConfigurableListableBeanFactory beanFactory;

    private Runtime pousseCafeRuntime;

    private void registerCoreComponents() {
        MessageSenderLocator messageSenderLocator = pousseCafeRuntime.messageSenderLocator();
        registerInstance(beanName(messageSenderLocator), messageSenderLocator);

        MessageFactory componentFactory = pousseCafeRuntime.environment().messageFactory();
        registerInstance(beanName(componentFactory), componentFactory);
    }

    private void registerAggregateServices() {
        for(AggregateServices services : pousseCafeRuntime.environment().aggregateServices()) {
            registerFactory(services);
            registerRepository(services);
        }
    }

    private void registerFactory(AggregateServices services) {
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

    private void registerRepository(AggregateServices services) {
        Repository<?, ?, ?> repository = services.getRepository();
        String beanName = beanName(repository);
        logger.debug("Registering repository {}", repository.getClass().getSimpleName());
        registerInstance(beanName, repository);
    }

    private void registerDomainProcesses() {
        for(DomainProcess process : pousseCafeRuntime.environment().domainProcesses()) {
            String beanName = beanName(process);
            logger.debug("Registering domain process {}", process.getClass().getSimpleName());
            registerInstance(beanName, process);
        }
    }

    private void registerServices() {
        for(Object service : pousseCafeRuntime.environment().services()) {
            String beanName = beanName(service);
            logger.debug("Registering service {}", service.getClass().getSimpleName());
            registerInstance(beanName, service);
        }
    }

    private Logger logger = LoggerFactory.getLogger(getClass());
}
