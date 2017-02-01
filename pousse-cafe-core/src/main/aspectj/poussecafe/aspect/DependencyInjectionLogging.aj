package poussecafe.aspect;

import org.slf4j.LoggerFactory;

public aspect DependencyInjectionLogging {

    pointcut injectDependencies(Object service):
        call(void poussecafe.configuration.Injector.injectDependencies(Object)) &&
        args(service);

    pointcut injectDependency(Object dependency):
        call(void poussecafe.configuration.DependencySetter.setResolvedDependency(Object)) &&
        args(dependency);

    before(Object service) : injectDependencies(service) {
        LoggerFactory
                .getLogger(thisJoinPoint.getTarget().getClass())
                .info("Injecting dependencies in " + service.getClass().getName());
    }
    
    before(Object dependency) : injectDependency(dependency) {
        LoggerFactory
                .getLogger(thisJoinPoint.getTarget().getClass())
                .debug("Injecting dependency " + dependency.getClass().getName());
    }
}
