package poussecafe.doc.model.domainprocessdoc;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import poussecafe.util.ReferenceEquals;

public class ToStep {

    public static class Builder {

        private ToStep toStep = new ToStep();

        public Builder name(StepName name) {
            toStep.name = name;
            return this;
        }

        public Builder directly(boolean directly) {
            toStep.directly = directly;
            return this;
        }

        public ToStep build() {
            return toStep;
        }
    }

    private ToStep() {

    }

    private StepName name;

    public StepName name() {
        return name;
    }

    private boolean directly;

    public boolean directly() {
        return directly;
    }

    @Override
    public boolean equals(Object obj) {
        return ReferenceEquals.referenceEquals(this, obj).orElse(other -> new EqualsBuilder()
                .append(name, other.name)
                .append(directly, other.directly)
                .build());
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(name)
                .append(directly)
                .build();
    }
}
