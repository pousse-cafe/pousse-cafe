package poussecafe.source.validation.model;

import java.io.Serializable;
import java.util.Optional;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import poussecafe.source.analysis.Name;
import poussecafe.source.generation.NamingConventions;
import poussecafe.source.validation.SourceLine;

import static java.util.Objects.requireNonNull;
import static poussecafe.util.Equality.referenceEquals;

@SuppressWarnings("serial")
public class DataAccessDefinition
implements Serializable, HasClassNameConvention {

    @Override
    public Optional<SourceLine> sourceLine() {
        return Optional.ofNullable(sourceLine);
    }

    private SourceLine sourceLine;

    @Override
    public Name className() {
        return className;
    }

    private Name className;

    @Override
    public boolean validClassName() {
        return NamingConventions.isDataAccessDefinitionName(className);
    }

    public static class Builder {

        public DataAccessDefinition build() {
            requireNonNull(dataAccessDefinition.className);
            return dataAccessDefinition;
        }

        private DataAccessDefinition dataAccessDefinition = new DataAccessDefinition();

        public Builder sourceLine(SourceLine sourceLine) {
            dataAccessDefinition.sourceLine = sourceLine;
            return this;
        }

        public Builder className(Name className) {
            dataAccessDefinition.className = className;
            return this;
        }
    }

    private DataAccessDefinition() {

    }

    @Override
    public boolean equals(Object obj) {
        return referenceEquals(this, obj).orElse(other -> new EqualsBuilder()
                .append(className, other.className)
                .append(sourceLine, other.sourceLine)
                .build());
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(className)
                .append(sourceLine)
                .build();
    }
}
