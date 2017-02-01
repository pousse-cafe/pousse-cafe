package poussecafe.configuration;

import java.lang.reflect.Method;
import poussecafe.consequence.Consequence;
import poussecafe.consequence.ConsequenceListener;
import poussecafe.consequence.ConsequenceListenerRoutingKey;
import poussecafe.consequence.Source;

public class ConsequenceListenerEntryBuilder {

    private Source source;

    private Class<? extends Consequence> consequenceClass;

    private String listenerId;

    private Method method;

    private Object target;

    public ConsequenceListenerEntry build() {
        ConsequenceListenerRoutingKey routingKey = new ConsequenceListenerRoutingKey(source, consequenceClass);
        ConsequenceListener listener = new ConsequenceListener(getOrGenerateListenerId(), method, target);
        return new ConsequenceListenerEntry(routingKey, listener);
    }

    private String getOrGenerateListenerId() {
        if (listenerId == null || listenerId.isEmpty()) {
            return generateListenerId();
        } else {
            return listenerId;
        }
    }

    private String generateListenerId() {
        return target.getClass().getCanonicalName() + "::" + method.getName() + "("
                + consequenceClass.getCanonicalName() + ")";
    }

    public ConsequenceListenerEntryBuilder withSource(Source source) {
        this.source = source;
        return this;
    }

    public ConsequenceListenerEntryBuilder withConsequenceClass(Class<? extends Consequence> consequenceClass) {
        this.consequenceClass = consequenceClass;
        return this;
    }

    public ConsequenceListenerEntryBuilder withListenerId(String listenerId) {
        this.listenerId = listenerId;
        return this;
    }

    public ConsequenceListenerEntryBuilder withMethod(Method method) {
        this.method = method;
        return this;
    }

    public ConsequenceListenerEntryBuilder withTarget(Object target) {
        this.target = target;
        return this;
    }
}
