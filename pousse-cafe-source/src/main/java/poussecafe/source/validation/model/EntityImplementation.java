package poussecafe.source.validation.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import poussecafe.source.analysis.ClassName;
import poussecafe.source.generation.NamingConventions;
import poussecafe.source.validation.SourceLine;

import static java.util.Objects.requireNonNull;
import static poussecafe.util.Equality.referenceEquals;

@SuppressWarnings("serial")
public class EntityImplementation
implements Serializable, HasClassNameConvention {

    private SourceLine sourceLine;

    @Override
    public Optional<SourceLine> sourceLine() {
        return Optional.ofNullable(sourceLine);
    }

    public Optional<String> entityImplementationQualifiedClassName() {
        return Optional.ofNullable(entityImplementationQualifiedClassName);
    }

    private String entityImplementationQualifiedClassName;

    public Optional<String> entityDefinitionQualifiedClassName() {
        return Optional.ofNullable(entityDefinitionQualifiedClassName);
    }

    private String entityDefinitionQualifiedClassName;

    public List<String> storageNames() {
        return Collections.unmodifiableList(storageNames);
    }

    private List<String> storageNames;

    @Override
    public boolean validClassName() {
        if(kind == StorageImplementationKind.ATTRIBUTES) {
            return NamingConventions.isEntityImplementationName(className());
        } else if(kind == StorageImplementationKind.DATA_ACCESS) {
            if(storageNames.size() == 1) {
                return NamingConventions.isDataAccessImplementationName(storageNames.get(0), className());
            } else {
                return true;
            }
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public StorageImplementationKind kind() {
        return kind;
    }

    private StorageImplementationKind kind;

    @Override
    public ClassName className() {
        if(kind == StorageImplementationKind.ATTRIBUTES) {
            return new ClassName(entityImplementationQualifiedClassName);
        } else if(kind == StorageImplementationKind.DATA_ACCESS) {
            return new ClassName(dataAccessImplementationClassName);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    private String dataAccessImplementationClassName;

    public boolean isConcrete() {
        return isConcrete;
    }

    private boolean isConcrete;

    public static class Builder {

        public EntityImplementation build() {
            requireNonNull(implementation.sourceLine);
            requireNonNull(implementation.storageNames);
            requireNonNull(implementation.kind);

            if(implementation.kind == StorageImplementationKind.DATA_ACCESS
                    && implementation.storageNames.size() != 1) {
                throw new IllegalStateException("A data access must be associated with a single storage");
            }

            return implementation;
        }

        private EntityImplementation implementation = new EntityImplementation();

        public Builder sourceFileLine(SourceLine sourceFileLine) {
            implementation.sourceLine = sourceFileLine;
            return this;
        }

        public Builder entityImplementationQualifiedClassName(Optional<String> entityImplementationQualifiedClassName) {
            implementation.entityImplementationQualifiedClassName = entityImplementationQualifiedClassName.orElse(null);
            return this;
        }

        public Builder entityDefinitionQualifiedClassName(Optional<String> entityDefinitionQualifiedClassName) {
            implementation.entityDefinitionQualifiedClassName = entityDefinitionQualifiedClassName.orElse(null);
            return this;
        }

        public Builder storageNames(List<String> storageNames) {
            implementation.storageNames = storageNames;
            return this;
        }

        public Builder kind(StorageImplementationKind kind) {
            implementation.kind = kind;
            return this;
        }

        public Builder dataAccessImplementationClassName(Optional<String> dataAccessImplementationClassName) {
            implementation.dataAccessImplementationClassName = dataAccessImplementationClassName.orElse(null);
            return this;
        }

        public Builder isConcrete(boolean isConcrete) {
            implementation.isConcrete = isConcrete;
            return this;
        }
    }

    private EntityImplementation() {

    }

    @Override
    public boolean equals(Object obj) {
        return referenceEquals(this, obj).orElse(other -> new EqualsBuilder()
                .append(entityDefinitionQualifiedClassName, other.entityDefinitionQualifiedClassName)
                .append(entityImplementationQualifiedClassName, other.entityImplementationQualifiedClassName)
                .append(dataAccessImplementationClassName, other.dataAccessImplementationClassName)
                .append(sourceLine, other.sourceLine)
                .append(storageNames, other.storageNames)
                .append(kind, other.kind)
                .build());
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(entityDefinitionQualifiedClassName)
                .append(entityImplementationQualifiedClassName)
                .append(dataAccessImplementationClassName)
                .append(sourceLine)
                .append(storageNames)
                .append(kind)
                .build();
    }
}
