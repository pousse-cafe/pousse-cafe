package poussecafe.configuration;

import java.lang.reflect.Method;
import poussecafe.consequence.DomainEventListener;

class DomainEventListeningServiceSourceProcessor extends ConsequenceListeningServiceSourceProcessor {

    public DomainEventListeningServiceSourceProcessor(Object service) {
        super(service);
    }

    @Override
    protected ConsequenceListenerAnnotationWrapper buildListenerAnnotationWrapper(Method method) {
        DomainEventListener annotation = method.getAnnotation(DomainEventListener.class);
        if (annotation == null) {
            return null;
        } else {
            return new DomainEventListenerAnnotationWrapper(annotation);
        }
    }

}
