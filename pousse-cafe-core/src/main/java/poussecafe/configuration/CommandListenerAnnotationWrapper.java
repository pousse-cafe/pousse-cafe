package poussecafe.configuration;

import java.lang.annotation.Annotation;
import poussecafe.messaging.CommandListener;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

class CommandListenerAnnotationWrapper implements MessageListenerAnnotationWrapper {

    private CommandListener annotation;

    public CommandListenerAnnotationWrapper(CommandListener annotation) {
        setAnnotation(annotation);
    }

    private void setAnnotation(CommandListener annotation) {
        checkThat(value(annotation).notNull().because("Wrapper annotation cannot be null"));
        this.annotation = annotation;
    }

    @Override
    public Class<? extends Annotation> getAnnotationClass() {
        return CommandListener.class;
    }

    @Override
    public String getListenerId() {
        return annotation.id();
    }

    @Override
    public String getSourceName() {
        return annotation.source();
    }

}
