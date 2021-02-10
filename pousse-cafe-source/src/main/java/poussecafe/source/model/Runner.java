package poussecafe.source.model;

import java.io.Serializable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import poussecafe.source.Source;
import poussecafe.source.analysis.ClassName;
import poussecafe.source.analysis.SafeClassName;

import static java.util.Objects.requireNonNull;
import static poussecafe.util.Equality.referenceEquals;

@SuppressWarnings("serial")
public class Runner implements Serializable, WithTypeComponent {

    public Source runnerSource() {
        return runnerSource;
    }

    private Source runnerSource;

    public String className() {
        return className;
    }

    private String className;

    @Override
    public TypeComponent typeComponent() {
        return new TypeComponent.Builder()
                .name(SafeClassName.ofRootClass(new ClassName(className)))
                .source(runnerSource)
                .build();
    }

    public static class Builder {

        private Runner messageListener = new Runner();

        public Runner build() {
            requireNonNull(messageListener.runnerSource);
            requireNonNull(messageListener.className);
            return messageListener;
        }

        public Builder withRunnerSource(Source runnerSource) {
            messageListener.runnerSource = runnerSource;
            return this;
        }

        public Builder withClassName(String className) {
            messageListener.className = className;
            return this;
        }
    }

    private Runner() {

    }

    @Override
    public boolean equals(Object obj) {
        return referenceEquals(this, obj).orElse(other -> new EqualsBuilder()
                .append(runnerSource, other.runnerSource)
                .append(className, other.className)
                .build());
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(runnerSource)
                .append(className)
                .build();
    }
}
