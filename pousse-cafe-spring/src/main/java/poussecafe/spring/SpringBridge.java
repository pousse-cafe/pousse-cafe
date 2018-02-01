package poussecafe.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Configuration;
import poussecafe.context.MetaApplicationContext;
import poussecafe.context.StorableServices;
import poussecafe.service.DomainProcess;

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
        String beanName = BEAN_NAME_PREFIX + services.getFactory().getClass().getSimpleName();
        logger.info("Registering factory {}", services.getFactory().getClass().getSimpleName());
        beanFactory.registerSingleton(beanName, services.getFactory());
    }

    private static final String BEAN_NAME_PREFIX = "pousseCafe";

    private void registeringRepository(StorableServices services) {
        String beanName = BEAN_NAME_PREFIX + services.getRepository().getClass().getSimpleName();
        logger.info("Registering repository {}", services.getRepository().getClass().getSimpleName());
        beanFactory.registerSingleton(beanName, services.getRepository());
    }

    private void registerDomainProcesses() {
        for(DomainProcess process : pousseCafeContext.getAllDomainProcesses()) {
            String beanName = BEAN_NAME_PREFIX + process.getClass().getSimpleName();
            logger.info("Registering domain process {}", process.getClass().getSimpleName());
            beanFactory.registerSingleton(beanName, process);
        }
    }

    private void registerServices() {
        for(Object service : pousseCafeContext.getAllServices()) {
            String beanName = BEAN_NAME_PREFIX + service.getClass().getSimpleName();
            logger.info("Registering service {}", service.getClass().getSimpleName());
            beanFactory.registerSingleton(beanName, service);
        }
    }

    private Logger logger = LoggerFactory.getLogger(getClass());

}
