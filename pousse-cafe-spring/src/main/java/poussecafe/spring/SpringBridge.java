package poussecafe.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Configuration;
import poussecafe.runtime.Runtime;

@Configuration
public class SpringBridge implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
        pousseCafeRuntime = beanFactory.getBean(Runtime.class);
        registerComponents();
    }

    private ConfigurableListableBeanFactory beanFactory;

    private Runtime pousseCafeRuntime;

    private void registerComponents() {
        pousseCafeRuntime.injector().injectableServices().forEach(this::registerInstance);
    }

    private void registerInstance(Object instance) {
        logger.debug("Registering {}", instance.getClass());
        beanFactory.registerSingleton(beanName(instance), instance);
    }

    private Logger logger = LoggerFactory.getLogger(getClass());

    private String beanName(Object instance) {
        return BEAN_NAME_PREFIX + instance.getClass().getName();
    }

    private static final String BEAN_NAME_PREFIX = "pousseCafe";
}
