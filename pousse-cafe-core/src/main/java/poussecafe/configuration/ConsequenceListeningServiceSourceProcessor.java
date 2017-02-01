package poussecafe.configuration;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import poussecafe.consequence.Consequence;
import poussecafe.consequence.Source;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

abstract class ConsequenceListeningServiceSourceProcessor {

    private Object service;

    public ConsequenceListeningServiceSourceProcessor(Object service) {
        setService(service);
    }

    private void setService(Object service) {
        checkThat(value(service).notNull().because("Service cannot be null"));
        this.service = service;
    }

    public List<ConsequenceListenerEntry> discoverListeners() {
        List<ConsequenceListenerEntry> entries = new ArrayList<>();
        for (Method method : service.getClass().getMethods()) {
            ConsequenceListenerEntry entry = tryDiscoverListener(method);
            if (entry != null) {
                entries.add(entry);
            }
        }
        return entries;
    }

    @SuppressWarnings("unchecked")
    private ConsequenceListenerEntry tryDiscoverListener(Method method) {
        ConsequenceListenerAnnotationWrapper annotationWrapper = buildListenerAnnotationWrapper(method);
        if (annotationWrapper != null) {
            ConsequenceListenerEntryBuilder builder = new ConsequenceListenerEntryBuilder();
            builder.withTarget(service);
            builder.withMethod(method);
            builder.withListenerId(annotationWrapper.getListenerId());
            builder.withSource(Source.forName(annotationWrapper.getSourceName()));
            builder.withConsequenceClass((Class<? extends Consequence>) method.getParameters()[0].getType());
            return builder.build();
        } else {
            return null;
        }
    }

    protected abstract ConsequenceListenerAnnotationWrapper buildListenerAnnotationWrapper(Method method);

}
