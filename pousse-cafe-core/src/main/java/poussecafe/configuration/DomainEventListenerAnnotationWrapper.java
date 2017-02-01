package poussecafe.configuration;

import java.lang.annotation.Annotation;
import poussecafe.consequence.DomainEventListener;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

class DomainEventListenerAnnotationWrapper implements ConsequenceListenerAnnotationWrapper {

    private DomainEventListener annotation;

    public DomainEventListenerAnnotationWrapper(DomainEventListener annotation) {
        setAnnotation(annotation);
    }

    private void setAnnotation(DomainEventListener annotation) {
        checkThat(value(annotation).notNull().because("Wrapper annotation cannot be null"));
        this.annotation = annotation;
    }

    @Override
    public Class<? extends Annotation> getAnnotationClass() {
        return DomainEventListener.class;
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
