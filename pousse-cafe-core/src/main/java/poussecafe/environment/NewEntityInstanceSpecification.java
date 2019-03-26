package poussecafe.environment;

import java.util.Objects;

public class NewEntityInstanceSpecification<T> {

    public static class Builder<T> {

        public Builder() {
            specification = new NewEntityInstanceSpecification<>();
        }

        private NewEntityInstanceSpecification<T> specification;

        public Builder<T> entityClass(Class<T> entityClass) {
            specification.entityClass = entityClass;
            return this;
        }

        public Builder<T> instantiateData(boolean instantiateData) {
            specification.instantiateData = instantiateData;
            return this;
        }

        public Builder<T> existingData(Object existingData) {
            specification.instantiateData = false;
            specification.existingData = existingData;
            return this;
        }

        public NewEntityInstanceSpecification<T> build() {
            Objects.requireNonNull(specification.entityClass);
            if(specification.instantiateData && specification.existingData != null) {
                throw new IllegalStateException("Provided data would be ignored");
            }
            return specification;
        }
    }

    private NewEntityInstanceSpecification() {

    }

    private Class<T> entityClass;

    private boolean instantiateData;

    public Class<T> entityClass() {
        return entityClass;
    }

    public boolean instantiateData() {
        return instantiateData;
    }

    public Object existingData() {
        return existingData;
    }

    private Object existingData;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((entityClass == null) ? 0 : entityClass.hashCode());
        result = prime * result + (instantiateData ? 1231 : 1237);
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
        @SuppressWarnings("rawtypes")
        NewEntityInstanceSpecification other = (NewEntityInstanceSpecification) obj;
        if (entityClass == null) {
            if (other.entityClass != null) {
                return false;
            }
        } else if (!entityClass.equals(other.entityClass)) {
            return false;
        }
        if (instantiateData != other.instantiateData) {
            return false;
        }
        return true;
    }
}
