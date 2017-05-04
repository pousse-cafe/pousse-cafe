package poussecafe.configuration;

import java.lang.reflect.Method;
import poussecafe.messaging.DomainEventListener;

class DomainEventListeningServiceSourceProcessor extends MessageListeningServiceSourceProcessor {

    public DomainEventListeningServiceSourceProcessor(Object service) {
        super(service);
    }

    @Override
    protected MessageListenerAnnotationWrapper buildListenerAnnotationWrapper(Method method) {
        DomainEventListener annotation = method.getAnnotation(DomainEventListener.class);
        if (annotation == null) {
            return null;
        } else {
            return new DomainEventListenerAnnotationWrapper(annotation);
        }
    }

}
