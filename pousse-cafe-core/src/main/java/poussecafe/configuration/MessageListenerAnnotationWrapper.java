package poussecafe.configuration;

import java.lang.annotation.Annotation;

interface MessageListenerAnnotationWrapper {

    Class<? extends Annotation> getAnnotationClass();

    String getListenerId();
}
