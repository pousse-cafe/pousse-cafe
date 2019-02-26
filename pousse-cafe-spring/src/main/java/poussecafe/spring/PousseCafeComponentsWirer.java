package poussecafe.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import poussecafe.process.DomainProcess;
import poussecafe.runtime.AggregateServices;
import poussecafe.runtime.Runtime;

@Component
public class PousseCafeComponentsWirer implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private Runtime pousseCafeRuntime;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        AutowireCapableBeanFactory beanFactory = event.getApplicationContext().getAutowireCapableBeanFactory();
        for(AggregateServices services : pousseCafeRuntime.environment().aggregateServices()) {
            logger.debug("Wiring services for entity {}", services.getEntityClass().getSimpleName());
            beanFactory.autowireBean(services.getRepository());
            beanFactory.autowireBean(services.getRepository().dataAccess());
            beanFactory.autowireBean(services.getFactory());
        }
        for(DomainProcess process : pousseCafeRuntime.environment().domainProcesses()) {
            logger.debug("Wiring domain process {}", process.getClass().getSimpleName());
            beanFactory.autowireBean(process);
        }
        for(Object service : pousseCafeRuntime.environment().services()) {
            logger.debug("Wiring service {}", service.getClass().getSimpleName());
            beanFactory.autowireBean(service);
        }
    }

    private Logger logger = LoggerFactory.getLogger(getClass());
}
