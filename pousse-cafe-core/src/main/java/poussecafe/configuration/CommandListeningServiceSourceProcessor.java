package poussecafe.configuration;

import java.lang.reflect.Method;
import poussecafe.messaging.CommandListener;

class CommandListeningServiceSourceProcessor extends MessageListeningServiceSourceProcessor {

    public CommandListeningServiceSourceProcessor(Object service) {
        super(service);
    }

    @Override
    protected MessageListenerAnnotationWrapper buildListenerAnnotationWrapper(Method method) {
        CommandListener annotation = method.getAnnotation(CommandListener.class);
        if (annotation == null) {
            return null;
        } else {
            return new CommandListenerAnnotationWrapper(annotation);
        }
    }

}
