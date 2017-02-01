package poussecafe.configuration;

import java.lang.annotation.Annotation;

interface ConsequenceListenerAnnotationWrapper {

    Class<? extends Annotation> getAnnotationClass();

    String getListenerId();

    String getSourceName();
}
