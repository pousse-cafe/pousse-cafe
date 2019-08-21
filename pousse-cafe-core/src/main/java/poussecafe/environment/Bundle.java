package poussecafe.environment;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import poussecafe.messaging.MessageImplementation;

public class Bundle {

    public static class Builder {

        private Bundle bundle = new Bundle();

        public Builder definition(BundleDefinition definition) {
            bundle.definition = definition;
            return this;
        }

        public Builder storageImplementations(Collection<EntityImplementation> storageImplementations) {
            bundle.storageImplementations.addAll(storageImplementations);
            return this;
        }

        public Builder withEntityImplementation(EntityImplementation entityImplementation) {
            bundle.storageImplementations.add(entityImplementation);
            return this;
        }

        public Builder messagingImplementations(Collection<MessageImplementation> messagingImplementations) {
            bundle.messagingImplementations.addAll(messagingImplementations);
            return this;
        }

        public Builder withMessageImplentation(MessageImplementation messageImplementation) {
            bundle.messagingImplementations.add(messageImplementation);
            return this;
        }

        public Builder serviceImplementations(Collection<ServiceImplementation> serviceImplementations) {
            bundle.serviceImplementations.addAll(serviceImplementations);
            return this;
        }

        public Bundle build() {
            Objects.requireNonNull(bundle.definition);
            Objects.requireNonNull(bundle.storageImplementations);
            Objects.requireNonNull(bundle.messagingImplementations);
            Objects.requireNonNull(bundle.serviceImplementations);
            return bundle;
        }
    }

    private Bundle() {

    }

    private BundleDefinition definition;

    public BundleDefinition definition() {
        return definition;
    }

    private Set<EntityImplementation> storageImplementations = new HashSet<>();

    public Set<EntityImplementation> entityImplementations() {
        return storageImplementations;
    }

    private Set<MessageImplementation> messagingImplementations = new HashSet<>();

    public Set<MessageImplementation> messageImplementations() {
        return messagingImplementations;
    }

    private Set<ServiceImplementation> serviceImplementations = new HashSet<>();

    public Set<ServiceImplementation> serviceImplementations() {
        return serviceImplementations;
    }
}
