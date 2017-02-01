package poussecafe.consequence;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;
import static poussecafe.check.Predicates.emptyOrNullString;
import static poussecafe.check.Predicates.not;

public class ConsequenceListener {

    private String listenerId;

    private Method method;

    private Object target;

    public ConsequenceListener(String listenerId, Method method, Object target) {
        setListenerId(listenerId);
        setMethod(method);
        setTarget(target);
    }

    public Method getMethod() {
        return method;
    }

    private void setMethod(Method method) {
        checkThat(value(method).notNull().because("Method cannot be null"));
        this.method = method;
    }

    public Object getTarget() {
        return target;
    }

    private void setTarget(Object target) {
        checkThat(value(target).notNull().because("Target cannot be null"));
        this.target = target;
    }

    public String getListenerId() {
        return listenerId;
    }

    private void setListenerId(String listenerId) {
        checkThat(value(listenerId).verifies(not(emptyOrNullString())).because("Listener ID cannot be empty"));
        this.listenerId = listenerId;
    }

    public void consume(Consequence consequence) {
        try {
            method.invoke(target, consequence);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throwInvokeError(e);
        }
    }

    private void throwInvokeError(Exception e) {
        throw new ConsequenceConsumptionException("Unable to invoke listener", e);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((method == null) ? 0 : method.hashCode());
        result = prime * result + ((target == null) ? 0 : target.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ConsequenceListener other = (ConsequenceListener) obj;
        if (method == null) {
            if (other.method != null) {
                return false;
            }
        } else if (!method.equals(other.method)) {
            return false;
        }
        if (target == null) {
            if (other.target != null) {
                return false;
            }
        } else if (!target.equals(other.target)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ConsequenceListener [listenerId=" + listenerId + ", method=" + method + ", target=" + target + "]";
    }

}
