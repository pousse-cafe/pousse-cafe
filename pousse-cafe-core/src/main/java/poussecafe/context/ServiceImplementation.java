package poussecafe.context;

import java.util.Objects;
import poussecafe.domain.Service;

public class ServiceImplementation {

    public static class Builder {

        private ServiceImplementation implementation = new ServiceImplementation();

        public Builder serviceClass(Class<? extends Service> serviceClass) {
            implementation.serviceClass = serviceClass;
            return this;
        }

        public Builder serviceImplementationClass(Class<? extends Service> serviceImplementationClass) {
            implementation.serviceImplementationClass = serviceImplementationClass;
            return this;
        }

        public ServiceImplementation build() {
            Objects.requireNonNull(implementation.serviceClass);
            Objects.requireNonNull(implementation.serviceImplementationClass);
            return implementation;
        }
    }

    private ServiceImplementation() {

    }

    private Class<? extends Service> serviceClass;

    public Class<? extends Service> serviceClass() {
        return serviceClass;
    }

    private Class<? extends Service> serviceImplementationClass;

    public Class<? extends Service> serviceImplementationClass() {
        return serviceImplementationClass;
    }
}
