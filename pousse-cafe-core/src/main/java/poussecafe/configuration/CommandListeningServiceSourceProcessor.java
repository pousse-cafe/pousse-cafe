package poussecafe.configuration;

import java.lang.reflect.Method;
import poussecafe.consequence.CommandListener;

class CommandListeningServiceSourceProcessor extends ConsequenceListeningServiceSourceProcessor {

    public CommandListeningServiceSourceProcessor(Object service) {
        super(service);
    }

    @Override
    protected ConsequenceListenerAnnotationWrapper buildListenerAnnotationWrapper(Method method) {
        CommandListener annotation = method.getAnnotation(CommandListener.class);
        if (annotation == null) {
            return null;
        } else {
            return new CommandListenerAnnotationWrapper(annotation);
        }
    }

}
