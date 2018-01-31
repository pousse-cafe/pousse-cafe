package poussecafe.context;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import poussecafe.messaging.Message;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

abstract class MessageListeningServiceSourceProcessor {

    private Object service;

    public MessageListeningServiceSourceProcessor(Object service) {
        setService(service);
    }

    private void setService(Object service) {
        checkThat(value(service).notNull().because("Service cannot be null"));
        this.service = service;
    }

    public List<MessageListenerEntry> discoverListeners() {
        List<MessageListenerEntry> entries = new ArrayList<>();
        for (Method method : service.getClass().getMethods()) {
            MessageListenerEntry entry = tryDiscoverListener(method);
            if (entry != null) {
                entries.add(entry);
            }
        }
        return entries;
    }

    @SuppressWarnings("unchecked")
    private MessageListenerEntry tryDiscoverListener(Method method) {
        MessageListenerAnnotationWrapper annotationWrapper = buildListenerAnnotationWrapper(method);
        if (annotationWrapper != null) {
            MessageListenerEntryBuilder builder = new MessageListenerEntryBuilder();
            builder.withTarget(service);
            builder.withMethod(method);
            builder.withListenerId(annotationWrapper.getListenerId());
            builder.withMessageClass((Class<? extends Message>) method.getParameters()[0].getType());
            return builder.build();
        } else {
            return null;
        }
    }

    protected abstract MessageListenerAnnotationWrapper buildListenerAnnotationWrapper(Method method);

}
