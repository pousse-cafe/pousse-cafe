package poussecafe.source.validation;

import java.io.Serializable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import poussecafe.source.Source;

import static java.util.Objects.requireNonNull;
import static poussecafe.util.Equality.referenceEquals;

@SuppressWarnings("serial")
public class SourceLine implements Serializable {

    public Source source() {
        return source;
    }

    private Source source;

    public int line() {
        return line;
    }

    private int line = -1;

    public static class Builder {

        public SourceLine build() {
            requireNonNull(sourceLine.source);
            if(sourceLine.line < 0) {
                throw new IllegalStateException();
            }
            return sourceLine;
        }

        private SourceLine sourceLine = new SourceLine();

        public Builder source(Source source) {
            sourceLine.source = source;
            return this;
        }

        public Builder line(int line) {
            sourceLine.line = line;
            return this;
        }
    }

    private SourceLine() {

    }

    @Override
    public String toString() {
        return source.toString() + " at line " + line;
    }

    @Override
    public boolean equals(Object obj) {
        return referenceEquals(this, obj).orElse(other -> new EqualsBuilder()
                .append(source, other.source)
                .append(line, other.line)
                .build());
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(source)
                .append(line)
                .build();
    }
}
