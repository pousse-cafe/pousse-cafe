package poussecafe.doc.model.domainprocessdoc;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import poussecafe.domain.DomainException;

import java.util.Objects;

public class ComponentMethodName {

    public static class Builder {

        private ComponentMethodName signature = new ComponentMethodName();

        public Builder componentName(String aggregateName) {
            signature.componentName = aggregateName;
            return this;
        }

        public Builder methodName(String methodName) {
            signature.methodName = methodName;
            return this;
        }

        public ComponentMethodName build() {
            Objects.requireNonNull(signature.componentName);
            Objects.requireNonNull(signature.methodName);
            return signature;
        }
    }

    private ComponentMethodName() {

    }

    private String componentName;

    public String componentName() {
        return componentName;
    }

    private String methodName;

    public String methodName() {
        return methodName;
    }

    @Override
    public String toString() {
        return componentName + "." + methodName;
    }

    public static ComponentMethodName parse(String signatureString) {
        String[] parts = signatureString.split("\\.");
        if(parts.length != 2) {
            throw new DomainException("Wrong signature format: " + signatureString);
        }

        String aggregateName = parts[0];
        String methodName = parts[1];
        return new ComponentMethodName.Builder()
                .componentName(aggregateName)
                .methodName(methodName)
                .build();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(componentName)
                .append(methodName)
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
        ComponentMethodName other = (ComponentMethodName) obj;
        return new EqualsBuilder()
                .append(componentName, other.componentName)
                .append(methodName, other.methodName)
                .build();
    }
}
