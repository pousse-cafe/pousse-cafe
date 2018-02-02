package poussecafe.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Configuration;
import poussecafe.context.MetaApplicationContext;
import poussecafe.context.StorableServices;
import poussecafe.service.DomainProcess;
import poussecafe.storable.IdentifiedStorableRepository;

@Configuration
public class SpringBridge implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
        pousseCafeContext = beanFactory.getBean(MetaApplicationContext.class);
        registerStorableServices();
        registerDomainProcesses();
        registerServices();
    }

    private ConfigurableListableBeanFactory beanFactory;

    private MetaApplicationContext pousseCafeContext;

    private void registerStorableServices() {
        for(StorableServices services : pousseCafeContext.getAllStorableServices()) {
            registeringFactory(services);
            registeringRepository(services);
        }
    }

    private void registeringFactory(StorableServices services) {
        String beanName = beanName(services.getFactory());
        logger.info("Registering factory {}", services.getFactory().getClass().getSimpleName());
        registerInstance(beanName, services.getFactory());
    }

    private String beanName(Object instance) {
        return BEAN_NAME_PREFIX + instance.getClass().getSimpleName();
    }

    private static final String BEAN_NAME_PREFIX = "pousseCafe";

    private void registerInstance(String beanName, Object instance) {
        beanFactory.registerSingleton(beanName, instance);
    }

    private void registeringRepository(StorableServices services) {
        IdentifiedStorableRepository<?, ?, ?> repository = services.getRepository();
        String beanName = beanName(repository);
        logger.info("Registering repository {}", repository.getClass().getSimpleName());
        registerInstance(beanName, repository);
    }

    private void registerDomainProcesses() {
        for(DomainProcess process : pousseCafeContext.getAllDomainProcesses()) {
            String beanName = beanName(process);
            logger.info("Registering domain process {}", process.getClass().getSimpleName());
            registerInstance(beanName, process);
        }
    }

    private void registerServices() {
        for(Object service : pousseCafeContext.getAllServices()) {
            String beanName = beanName(service);
            logger.info("Registering service {}", service.getClass().getSimpleName());
            registerInstance(beanName, service);
        }
    }

    private Logger logger = LoggerFactory.getLogger(getClass());
}
