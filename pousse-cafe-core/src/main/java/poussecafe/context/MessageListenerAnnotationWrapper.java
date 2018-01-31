package poussecafe.context;

import java.lang.annotation.Annotation;

interface MessageListenerAnnotationWrapper {

    Class<? extends Annotation> getAnnotationClass();

    String getListenerId();
}
