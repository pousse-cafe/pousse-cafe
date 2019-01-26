package poussecafe.doc.model.step;

import java.util.Optional;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import poussecafe.doc.model.domainprocessdoc.ComponentMethodName;
import poussecafe.domain.DomainException;

import java.util.Objects;

public class StepMethodSignature {

    public static class Builder {

        private StepMethodSignature signature = new StepMethodSignature();

        public Builder componentMethodName(ComponentMethodName componentMethodName) {
            signature.componentMethodName = componentMethodName;
            return this;
        }

        public Builder consumedEventName(Optional<String> consumedEventName) {
            signature.consumedEventName = consumedEventName;
            return this;
        }

        public StepMethodSignature build() {
            Objects.requireNonNull(signature.componentMethodName);
            Objects.requireNonNull(signature.consumedEventName);
            return signature;
        }
    }

    private StepMethodSignature() {

    }

    private ComponentMethodName componentMethodName;

    public ComponentMethodName componentMethodName() {
        return componentMethodName;
    }

    private Optional<String> consumedEventName = Optional.empty();

    public Optional<String> consumedEventName() {
        return consumedEventName;
    }

    @Override
    public String toString() {
        if(consumedEventName.isPresent()) {
            return componentMethodName.toString() + "(" + consumedEventName.get() + ")";
        } else {
            return componentMethodName.toString() + "()";
        }
    }

    public static StepMethodSignature parse(String signatureString) {
        int indexOfOpen = signatureString.indexOf('(');
        int indexOfClose = signatureString.indexOf(')');
        if(indexOfOpen == -1 || indexOfClose == -1 || indexOfClose != signatureString.length() - 1 || indexOfOpen > indexOfClose) {
            throw new DomainException("Wrong signature format: " + signatureString);
        } else {
            ComponentMethodName aggregateMethodName = ComponentMethodName.parse(signatureString.substring(0, indexOfOpen));
            String consumedEventName = signatureString.substring(indexOfOpen + 1, indexOfClose);
            Optional<String> consumedEvent = Optional.empty();
            if(consumedEventName.length() > 0) {
                consumedEvent = Optional.of(consumedEventName);
            }
            return new StepMethodSignature.Builder()
                    .componentMethodName(aggregateMethodName)
                    .consumedEventName(consumedEvent)
                    .build();
        }
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(componentMethodName)
                .append(consumedEventName)
                .build();
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) {
            return true;
        }
        if(obj == null) {
            return false;
        }
        if(getClass() != obj.getClass()) {
            return false;
        }
        StepMethodSignature other = (StepMethodSignature) obj;
        return new EqualsBuilder()
                .append(componentMethodName, other.componentMethodName)
                .append(consumedEventName, other.consumedEventName)
                .build();
    }
}
