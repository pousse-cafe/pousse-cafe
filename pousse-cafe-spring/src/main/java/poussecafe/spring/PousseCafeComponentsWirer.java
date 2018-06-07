package poussecafe.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import poussecafe.context.MetaApplicationContext;
import poussecafe.context.StorableServices;
import poussecafe.process.DomainProcess;

@Component
public class PousseCafeComponentsWirer implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private MetaApplicationContext pousseCafeContext;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        AutowireCapableBeanFactory beanFactory = event.getApplicationContext().getAutowireCapableBeanFactory();
        for(StorableServices services : pousseCafeContext.getAllStorableServices()) {
            logger.debug("Wiring services for storable {}", services.getStorableClass().getSimpleName());
            beanFactory.autowireBean(services.getRepository());
            beanFactory.autowireBean(services.getRepository().getDataAccess());
            beanFactory.autowireBean(services.getFactory());
        }
        for(DomainProcess process : pousseCafeContext.getAllDomainProcesses()) {
            logger.debug("Wiring domain process {}", process.getClass().getSimpleName());
            beanFactory.autowireBean(process);
        }
        for(Object service : pousseCafeContext.getAllServices()) {
            logger.debug("Wiring service {}", service.getClass().getSimpleName());
            beanFactory.autowireBean(service);
        }
    }

    private Logger logger = LoggerFactory.getLogger(getClass());
}
